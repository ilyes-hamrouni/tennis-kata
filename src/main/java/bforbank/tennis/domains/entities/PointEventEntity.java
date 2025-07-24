package bforbank.tennis.domains.entities;

import bforbank.tennis.domains.enums.TeamType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Data
@Entity
@Table(name = "point_events")
public class PointEventEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @Column(nullable = false)
    private int sequenceNumber;

    @Enumerated(EnumType.STRING)
    private TeamType winningTeam;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;
}