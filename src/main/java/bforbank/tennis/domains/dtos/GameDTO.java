package bforbank.tennis.domains.dtos;


import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "Game data transfer object")
@Data
public class GameDTO {
    private Long id;

    private Long setId;

    private List<PlayerDTO> teamA;
    private List<PlayerDTO> teamB;

    private int teamAPoints;
    private int teamBPoints;

    private boolean teamAHasAdvantage;
    private boolean teamBHasAdvantage;

    private GameStatus status;

    private TeamType winnerTeam;

    private List<PointEventDTO> history;

    private String score;
}