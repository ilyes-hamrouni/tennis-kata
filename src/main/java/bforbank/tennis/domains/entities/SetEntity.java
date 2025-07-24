package bforbank.tennis.domains.entities;

import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "sets")
public class SetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    @Column(name = "team_a_games_won")
    private int teamAGamesWon;

    @Column(name = "team_b_games_won")
    private int teamBGamesWon;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Enumerated(EnumType.STRING)
    private TeamType winner;


    @ManyToMany
    @JoinTable(name = "set_team_a_players", // Renamed to avoid conflict
            joinColumns = @JoinColumn(name = "set_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private List<PlayerEntity> teamA;

    @ManyToMany
    @JoinTable(name = "set_team_b_players", // Renamed to avoid conflict
            joinColumns = @JoinColumn(name = "set_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private List<PlayerEntity> teamB;



    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameEntity> games = new ArrayList<>();


}
