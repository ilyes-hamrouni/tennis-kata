package bforbank.tennis.domains.entities;

import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "games")
public class GameEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "game_team_a_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<PlayerEntity> teamA;

    @ManyToMany
    @JoinTable(
            name = "game_team_b_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<PlayerEntity> teamB;

    /** Raw point counts for each side */
    @Column(name = "team_a_points", nullable = false)
    private int teamAPoints = 0;

    @Column(name = "team_b_points", nullable = false)
    private int teamBPoints = 0;

    @Column(name = "team_a_advantage", nullable = false)
    private boolean teamAHasAdvantage = false;

    @Column(name = "team_b_advantage", nullable = false)
    private boolean teamBHasAdvantage = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status = GameStatus.IN_PROGRESS;

    @Enumerated(EnumType.STRING)
    private TeamType winnerTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id", nullable = false)
    private SetEntity set;

    @OneToMany(
            mappedBy = "game",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("sequenceNumber ASC")
    private List<PointEventEntity> history;

    @Column(name="score")
    private String score;

}
