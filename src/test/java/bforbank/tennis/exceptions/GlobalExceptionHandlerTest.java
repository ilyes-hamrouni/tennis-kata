package bforbank.tennis.exceptions;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DummyController.class)
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnErrorResponse_whenTennisExceptionThrown() throws Exception {
        mockMvc.perform(get("/throw"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(TennisErrorConstants.PLAYER_NOT_FOUND))
                .andExpect(jsonPath("$.path").value("/throw"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldFallbackTo500_whenHttpStatusIsInvalid() throws Exception {
        mockMvc.perform(get("/throw-invalid"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(999))
                .andExpect(jsonPath("$.errorMessage").value("Invalid HTTP code"))
                .andExpect(jsonPath("$.path").value("/throw-invalid"));
    }
}
