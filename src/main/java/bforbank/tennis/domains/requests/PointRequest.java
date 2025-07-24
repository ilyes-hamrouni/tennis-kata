package bforbank.tennis.domains.requests;

import bforbank.tennis.domains.enums.TeamType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PointRequest {
    @NotNull
    private TeamType teamWinner;

}

