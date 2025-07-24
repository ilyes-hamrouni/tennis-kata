package bforbank.tennis.controllers;

import bforbank.tennis.domains.dtos.GameDTO;
import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import bforbank.tennis.domains.requests.CreateGameRequest;
import bforbank.tennis.domains.requests.PointRequest;
import bforbank.tennis.services.GameService;
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

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateGame() throws Exception {
        CreateGameRequest request = new CreateGameRequest();
        request.setTeamBIds(List.of(1L));
        request.setTeamAIds(List.of(2L));
        request.setStadium("Wimbeldon");

        GameDTO mockResponse = new GameDTO();
        mockResponse.setId(100L);
        mockResponse.setStatus(GameStatus.IN_PROGRESS);

        when(gameService.createGame(ArgumentMatchers.any(CreateGameRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testGetGame() throws Exception {
        GameDTO mockResponse = new GameDTO();
        mockResponse.setId(10L);
        mockResponse.setStatus(GameStatus.IN_PROGRESS);

        when(gameService.getGame(10L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/games/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testRecordPoint() throws Exception {
        PointRequest request = new PointRequest();
        request.setTeamWinner(TeamType.TEAM_A);

        GameDTO mockResponse = new GameDTO();
        mockResponse.setId(200L);
        mockResponse.setScore("15-0");

        when(gameService.recordPoint(200L, request)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/games/200/point")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.score").value("15-0"));
    }

    @Test
    void testRecordMultiplePoints() throws Exception {
        var point1= new PointRequest();
        point1.setTeamWinner(TeamType.TEAM_A);
        var point2= new PointRequest();
        point2.setTeamWinner(TeamType.TEAM_B);

        List<PointRequest> points = List.of(
                point1,point2
        );

        GameDTO mockResponse = new GameDTO();
        mockResponse.setId(500L);
        mockResponse.setScore("30-0");

        when(gameService.recordMultiplePoints(500L, points)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/games/500/multiple-points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(points)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(500))
                .andExpect(jsonPath("$.score").value("30-0"));
    }
}
