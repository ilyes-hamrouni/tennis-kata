package bforbank.tennis.controllers;


import bforbank.tennis.domains.dtos.MatchDTO;
import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import bforbank.tennis.domains.requests.MatchRequest;
import bforbank.tennis.services.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchService matchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateMatch() throws Exception {
        MatchRequest request = new MatchRequest(
                List.of(1L), List.of(2L), "Center Court"
        );

        MatchDTO response = new MatchDTO();
        response.setId(1L);
        response.setStadium("Center Court");
        response.setStatus(GameStatus.IN_PROGRESS);
        response.setWinner(null);

        when(matchService.createMatch(ArgumentMatchers.any(MatchRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stadium").value("Center Court"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testGetMatchById() throws Exception {
        MatchDTO response = new MatchDTO();
        response.setId(1L);
        response.setWinner(TeamType.TEAM_A);
        response.setStatus(GameStatus.FINISHED);

        when(matchService.getMatch(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.winner").value("TEAM_A"))
                .andExpect(jsonPath("$.status").value("FINISHED"));
    }

    @Test
    void testGetAllMatches() throws Exception {
        MatchDTO match1 = new MatchDTO();
        match1.setId(1L);
        match1.setWinner(TeamType.TEAM_A);

        MatchDTO match2 = new MatchDTO();
        match2.setId(2L);
        match2.setWinner(TeamType.TEAM_B);

        when(matchService.getAllMatches()).thenReturn(List.of(match1, match2));

        mockMvc.perform(get("/api/v1/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].winner").value("TEAM_A"))
                .andExpect(jsonPath("$[1].winner").value("TEAM_B"));
    }
}

