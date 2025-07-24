package bforbank.tennis.domains.requests;

import bforbank.tennis.domains.enums.TeamType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PointRequest {
    @Schema(description = "Team that won the point, e.g. 'A' or 'B'", example = "A", required = true)
    @NotNull
    private TeamType teamWinner;

}

