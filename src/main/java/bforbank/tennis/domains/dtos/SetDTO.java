package bforbank.tennis.domains.dtos;

import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.util.List;

@Schema(description = "Representation of a Set within a Match")
@Data
public class SetDTO {

    @Schema(description = "Unique ID of the set", example = "5")
    private Long id;

    @Schema(description = "ID of the parent match", example = "1", required = true)
    private Long matchId;

    @Schema(description = "Number of games won by Team A", example = "4")
    private int teamAGamesWon;

    @Schema(description = "Number of games won by Team B", example = "2")
    private int teamBGamesWon;

    @Schema(description = "Current status of the set", example = "IN_PROGRESS")
    private GameStatus status;

    @Schema(description = "Winning team of the set, if any", example = "TEAM_A")
    private TeamType winner;

    @ElementCollection
    private List<PlayerDTO> teamA;

    @ElementCollection
    private List<PlayerDTO> teamB;

    @Schema(description = "List of games played in the set (optional)")
    private List<GameDTO> games;

}
