package bforbank.tennis.domains.requests;


import bforbank.tennis.domains.dtos.PlayerDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PointSequenceRequest {
    @NotNull
    private Long gameId;

    @NotEmpty
    private List<PlayerDTO> sequence;
}
