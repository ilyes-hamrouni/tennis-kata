package bforbank.tennis.services;

import bforbank.tennis.domains.dtos.GameDTO;
import bforbank.tennis.domains.dtos.SetDTO;
import bforbank.tennis.domains.entities.GameEntity;
import bforbank.tennis.domains.entities.MatchEntity;
import bforbank.tennis.domains.entities.SetEntity;
import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.mappers.GameMapper;
import bforbank.tennis.mappers.SetMapper;
import bforbank.tennis.repositories.GameRepository;
import bforbank.tennis.repositories.MatchRepository;
import bforbank.tennis.repositories.SetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static bforbank.tennis.exceptions.TennisErrorConstants.*;

@Slf4j
@Service
public class SetService {


    private final SetRepository setRepository;
    private final MatchRepository matchRepository;
    private final SetMapper setMapper;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;

    public SetService(SetRepository setRepository, MatchRepository matchRepository, SetMapper setMapper, GameMapper gameMapper,  GameRepository gameRepository) {
        this.setRepository = setRepository;
        this.matchRepository = matchRepository;
        this.setMapper = setMapper;
        this.gameMapper = gameMapper;
        this.gameRepository = gameRepository;
    }

    public SetDTO createSet(Long matchId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    log.error(ERROR_MATCH_NOT_FOUND);
                    return new TennisException(404, ERROR_SET_NOT_FOUND);
                });

        SetEntity set = new SetEntity();
        set.setMatch(match);
        set.setStatus(GameStatus.IN_PROGRESS);
        set.setTeamA(new ArrayList<>(match.getTeamA()));
        set.setTeamB(new ArrayList<>(match.getTeamB()));

        return setMapper.toDTO(setRepository.save(set));
    }


    public List<SetDTO> getSetsByMatch(Long matchId) {
        return setRepository.findByMatchId(matchId).stream()
                .map(setMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void recordGameWin(Long setId, TeamType winner) {
        SetEntity set = setRepository.findById(setId)
                .orElseThrow(() -> new TennisException(404, ERROR_MATCH_NOT_FOUND));

        if (set.getStatus() == GameStatus.FINISHED) return;

        if (winner == TeamType.TEAM_A) {
            set.setTeamAGamesWon(set.getTeamAGamesWon() + 1);
        } else {
            set.setTeamBGamesWon(set.getTeamBGamesWon() + 1);
        }

        int a = set.getTeamAGamesWon();
        int b = set.getTeamBGamesWon();

        if ((a >= 6 || b >= 6) && Math.abs(a - b) >= 2) {
            set.setStatus(GameStatus.FINISHED);
            set.setWinner(a > b ? TeamType.TEAM_A : TeamType.TEAM_B);
        }

        setRepository.save(set);
    }

    public GameDTO createGameInSet(Long setId) {
        SetEntity set = setRepository.findById(setId)
                .orElseThrow(() -> new TennisException(404, ERROR_SET_NOT_FOUND));

        if (set.getStatus() == GameStatus.FINISHED) {
            throw new TennisException(409, ERROR_CREATE_GAME_SET_IS_FINISHED);
        }

        GameEntity game = new GameEntity();
        game.setSet(set);
        game.setStatus(GameStatus.IN_PROGRESS);
        return gameMapper.toDTO(gameRepository.save(game));
    }

    public GameDTO getLatestGameInSet(Long setId) {
        List<GameEntity> games = gameRepository.findBySetId(setId);
        return games.stream()
                .max(Comparator.comparing(GameEntity::getId))
                .map(gameMapper::toDTO)
                .orElseThrow(() -> new TennisException(404, ERROR_GAME_NOT_FOUND));
    }

    public SetDTO getSetById(Long id) {
        return setMapper.toDTO(setRepository.findById(id)
                .orElseThrow(() -> new TennisException(404, ERROR_SET_NOT_FOUND)));
    }


}
