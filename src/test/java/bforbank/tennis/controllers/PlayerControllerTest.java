package bforbank.tennis.controllers;


import bforbank.tennis.domains.dtos.PlayerDTO;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.services.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static bforbank.tennis.exceptions.TennisErrorConstants.ERROR_PLAYER_NOT_FOUND;
import static org.hamcrest.Matchers.*;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_shouldReturnPlayerDTO_whenValidInput() throws Exception {
        PlayerDTO inputDto = Instancio.of(PlayerDTO.class)
                .set(field("name"), "Djokovic")
                .create();

        PlayerDTO outputDto = Instancio.create(PlayerDTO.class);

        when(playerService.create(any(PlayerDTO.class))).thenReturn(outputDto);

        mockMvc.perform(post("/api/v1/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", notNullValue()));
    }

    @Test
    void getAll_shouldReturnListOfPlayers() throws Exception {
        List<PlayerDTO> players = Instancio.ofList(PlayerDTO.class).size(2).create();
        when(playerService.findAll()).thenReturn(players);

        mockMvc.perform(get("/api/v1/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void getById_shouldReturnPlayer_whenExists() throws Exception {
        PlayerDTO dto = Instancio.create(PlayerDTO.class);
        when(playerService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", notNullValue()));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        when(playerService.findById(99L)).thenThrow(new TennisException(404, ERROR_PLAYER_NOT_FOUND));

        mockMvc.perform(get("/api/v1/players/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnNoContent_whenExists() throws Exception {
        mockMvc.perform(delete("/api/v1/players/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new TennisException(404, ERROR_PLAYER_NOT_FOUND)).when(playerService).delete(42L);

        mockMvc.perform(delete("/api/v1/players/42"))
                .andExpect(status().isNotFound());
    }
}
