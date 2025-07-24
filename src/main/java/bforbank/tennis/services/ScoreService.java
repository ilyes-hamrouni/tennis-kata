package bforbank.tennis.services;

import bforbank.tennis.domains.entities.GameEntity;
import bforbank.tennis.domains.entities.MatchEntity;
import bforbank.tennis.domains.entities.SetEntity;
import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.repositories.GameRepository;
import bforbank.tennis.repositories.MatchRepository;
import bforbank.tennis.repositories.SetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static bforbank.tennis.exceptions.TennisErrorConstants.ERROR_SET_NOT_FOUND;

@Service
public class ScoreService {

    private final SetRepository setRepository;
    private final GameRepository gameRepository;
    private final MatchRepository matchRepository;

    public ScoreService(SetRepository setRepository,
                        GameRepository gameRepository, MatchRepository matchRepository
    ) {
        this.setRepository = setRepository;
        this.gameRepository = gameRepository;

        this.matchRepository = matchRepository;
    }

    public void updateSetStatus(Long setId) {
        SetEntity set = setRepository.findById(setId)
                .orElseThrow(() -> new TennisException(404,ERROR_SET_NOT_FOUND));

        List<GameEntity> games = gameRepository.findBySetId(setId);

        long winsA = games.stream().filter(g -> g.getWinnerTeam() == TeamType.TEAM_A).count();
        long winsB = games.stream().filter(g -> g.getWinnerTeam() == TeamType.TEAM_B).count();

        set.setTeamAGamesWon((int) winsA);
        set.setTeamBGamesWon((int) winsB);

        if (winsA >= 6 && winsB <= 4) {
            set.setWinner(TeamType.TEAM_A);
            set.setStatus(GameStatus.FINISHED);
        } else if (winsB >= 6 && winsA <= 4) {
            set.setWinner(TeamType.TEAM_B);
            set.setStatus(GameStatus.FINISHED);
        } else if (winsA == 6 && winsB == 6) {
            set.setStatus(GameStatus.TIE_BREAK);
        } else if(winsA > 6 || winsB > 6) {
            set.setStatus(GameStatus.FINISHED); //todo create a special game for this.
        }
        else {
            set.setStatus(GameStatus.IN_PROGRESS);
        }


        setRepository.save(set);
        this.updateMatchAfterSet(set.getMatch());
    }
    public void updateMatchAfterSet(MatchEntity match) {
        long teamASetsWon = match.getSets().stream()
                .filter(s -> s.getWinner() == TeamType.TEAM_A).count();
        long teamBSetsWon = match.getSets().stream()
                .filter(s -> s.getWinner() == TeamType.TEAM_B).count();

        match.setTeamASetsWon((int) teamASetsWon);
        match.setTeamBSetsWon((int) teamBSetsWon);

        if (teamASetsWon+ teamBSetsWon ==3 ) {
            match.setStatus(GameStatus.FINISHED);
            match.setWinner(teamASetsWon > teamBSetsWon ? TeamType.TEAM_A : TeamType.TEAM_B);
        } else {
            match.setStatus(GameStatus.IN_PROGRESS);
        }

        matchRepository.save(match);
    }

}
