package bforbank.tennis.domains.dtos;

import bforbank.tennis.domains.enums.TeamType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PointEventDTO {
    private Long id;
    private Long gameId;
    private int sequenceNumber;
    private String scoreSnapshot;
    private TeamType winningTeam;
    private LocalDateTime timestamp;
}