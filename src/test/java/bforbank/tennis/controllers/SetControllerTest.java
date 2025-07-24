package bforbank.tennis.controllers;


import bforbank.tennis.domains.dtos.GameDTO;
import bforbank.tennis.domains.dtos.SetDTO;
import bforbank.tennis.domains.enums.TeamType;
import bforbank.tennis.services.SetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SetController.class)
class SetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SetService setService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateSet() throws Exception {
        SetDTO setDTO = new SetDTO();
        setDTO.setId(1L);

        when(setService.createSet(1L)).thenReturn(setDTO);

        mockMvc.perform(post("/api/v1/sets/match/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetSetsByMatch() throws Exception {
        SetDTO set1 = new SetDTO(); set1.setId(1L);
        SetDTO set2 = new SetDTO(); set2.setId(2L);

        when(setService.getSetsByMatch(1L)).thenReturn(List.of(set1, set2));

        mockMvc.perform(get("/api/v1/sets/match/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testRecordWin() throws Exception {
        mockMvc.perform(post("/api/v1/sets/1/record-win/TEAM_A"))
                .andExpect(status().isNoContent());

        verify(setService, times(1)).recordGameWin(1L, TeamType.TEAM_A);
    }

    @Test
    void testCreateGameInSet() throws Exception {
        GameDTO game = new GameDTO(); game.setId(10L);
        when(setService.createGameInSet(1L)).thenReturn(game);

        mockMvc.perform(post("/api/v1/sets/1/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void testGetLatestGameInSet() throws Exception {
        GameDTO game = new GameDTO(); game.setId(99L);
        when(setService.getLatestGameInSet(2L)).thenReturn(game);

        mockMvc.perform(get("/api/v1/sets/2/games/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99));
    }
}
