package bforbank.tennis.domains.dtos;


import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import lombok.Data;

import java.util.List;

@Data
public class GameDTO {
    private Long id;

    private String stadium;

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