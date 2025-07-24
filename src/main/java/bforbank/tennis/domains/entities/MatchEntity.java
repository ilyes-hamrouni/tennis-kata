package bforbank.tennis.domains.entities;

import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "match")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TeamType winner;

    private int teamASetsWon;
    private int teamBSetsWon;

    @Column(name="stadium")
    private String stadium;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @ManyToMany
    @JoinTable(
            name = "match_team_a_players",
            joinColumns = @JoinColumn(name = "match_id", referencedColumnName = "id", table = "match"),
            inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "id")
    )
    private List<PlayerEntity> teamA;

    @ManyToMany
    @JoinTable(
            name = "match_team_b_players",
            joinColumns = @JoinColumn(name = "match_id", referencedColumnName = "id", table = "match"),
            inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "id")
    )
    private List<PlayerEntity> teamB;


    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SetEntity> sets = new ArrayList<>();

}