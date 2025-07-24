package bforbank.tennis.controllers;


import bforbank.tennis.domains.dtos.GameDTO;
import bforbank.tennis.domains.requests.CreateGameRequest;
import bforbank.tennis.domains.requests.PointRequest;
import bforbank.tennis.services.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createGame_shouldReturn201CreatedWithBody() throws Exception {
        CreateGameRequest request = Instancio.create(CreateGameRequest.class);
        GameDTO dto = Instancio.create(GameDTO.class);

        when(gameService.createGame(request)).thenReturn(dto);

        mockMvc.perform(post("/api/v1/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void recordPoint_shouldReturn200WithUpdatedGame() throws Exception {
        Long gameId = 10L;
        PointRequest request = Instancio.create(PointRequest.class);
        GameDTO dto = Instancio.create(GameDTO.class);

        when(gameService.recordPoint(gameId, request)).thenReturn(dto);

        mockMvc.perform(post("/api/v1/games/{id}/point", gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void getGame_shouldReturnGame_whenFound() throws Exception {
        Long gameId = 99L;
        GameDTO dto = Instancio.create(GameDTO.class);

        when(gameService.getGame(gameId)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/games/{id}", gameId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    }