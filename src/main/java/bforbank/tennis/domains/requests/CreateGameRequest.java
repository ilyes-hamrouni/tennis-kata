package bforbank.tennis.domains.requests;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateGameRequest {
    @NotEmpty
    private List<Long> teamAIds;

    @NotEmpty
    private List<Long> teamBIds;

    private String stadium;
}