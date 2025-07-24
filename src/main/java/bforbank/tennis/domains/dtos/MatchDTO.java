package bforbank.tennis.domains.dtos;


import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "Match data transfer object")
@Data
public class MatchDTO {

    @Schema(description = "Match ID", example = "1")
    private Long id;

    @Schema(description = "Winner of the match", example = "TEAM_A")
    private TeamType winner;

    private int teamASetsWon;
    private int teamBSetsWon;

    private String stadium;

    private GameStatus status;


    private List<PlayerDTO> teamA;
    private List<PlayerDTO> teamB;

    @Schema(description = "List of sets in the match")
    private List<SetDTO> sets;

}
