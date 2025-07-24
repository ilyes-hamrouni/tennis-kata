package bforbank.tennis.services;

import bforbank.tennis.domains.entities.SetEntity;
import bforbank.tennis.exceptions.TennisErrorConstants;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.mappers.PointEventMapper;
import bforbank.tennis.domains.dtos.*;
import bforbank.tennis.domains.entities.GameEntity;
import bforbank.tennis.domains.entities.PlayerEntity;

import bforbank.tennis.mappers.GameMapper;

import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.requests.CreateGameRequest;
import bforbank.tennis.domains.requests.PointRequest;
import bforbank.tennis.repositories.GameRepository;
import bforbank.tennis.repositories.PointEventRepository;
import bforbank.tennis.repositories.PlayerRepository;
import bforbank.tennis.repositories.SetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PointEventRepository pointEventRepository;
    private final PlayerRepository playerRepository;
    private final GameMapper gameMapper;
    private final PointEventMapper pointEventMapper;
    private final ScoringStrategy scoringStrategy;
    private final ScoreService scoreService;
    private final SetRepository setRepository;

    public GameService(
            GameRepository gameRepository,
            PointEventRepository pointEventRepository, PlayerRepository playerRepository,
            GameMapper gameMapper, PointEventMapper pointEventMapper,
            ScoringStrategy scoringStrategy,
            ScoreService scoreService, SetRepository setRepository) {
        this.gameRepository = gameRepository;
        this.pointEventRepository = pointEventRepository;
        this.playerRepository = playerRepository;
        this.gameMapper = gameMapper;
        this.pointEventMapper = pointEventMapper;
        this.scoringStrategy = scoringStrategy;
        this.scoreService = scoreService;
        this.setRepository = setRepository;
    }

    public GameDTO createGame(Long setId) {
        SetEntity set = setRepository.findById(setId).orElseThrow();

        GameEntity game = new GameEntity();
        game.setSet(set);
        game.setStatus(GameStatus.IN_PROGRESS);

        return gameMapper.toDTO(gameRepository.save(game));
    }

    public GameDTO createGame(CreateGameRequest request) {
        List<PlayerEntity> teamAPlayers = request.getTeamAIds().stream()
                .map(id -> playerRepository.findById(id)
                        .orElseThrow(() -> new TennisException(
                                404,
                                String.format(TennisErrorConstants.ERROR_MSG_PLAYER_NOT_FOUND, id)
                        )))
                .collect(Collectors.toList());
        List<PlayerEntity> teamBPlayers = request.getTeamBIds().stream()
                .map(id -> playerRepository.findById(id)
                        .orElseThrow(() -> new TennisException(
                                404,
                                String.format(TennisErrorConstants.ERROR_MSG_PLAYER_NOT_FOUND, id)
                        )))
                .collect(Collectors.toList());
        GameEntity gameEntity = new GameEntity();
        gameEntity.setTeamA(teamAPlayers);
        gameEntity.setTeamB(teamBPlayers);
        gameEntity.setStatus(GameStatus.IN_PROGRESS);
        gameEntity = gameRepository.save(gameEntity);
        return gameMapper.toDTO(gameEntity);
    }

    public GameDTO getGame(Long gameId) {
        return gameMapper.toDTO(
                gameRepository.findById(gameId)
                        .orElseThrow(() -> new TennisException(
                                404,
                                TennisErrorConstants.ERROR_GAME_NOT_FOUND
                        ))
        );
    }

    @Transactional
    public GameDTO recordPoint(Long gameId, PointRequest request) {
        GameDTO game = gameMapper.toDTO(gameRepository.findById(gameId)
                .orElseThrow(() -> new TennisException(
                        404,
                        TennisErrorConstants.ERROR_GAME_NOT_FOUND
                )));

        scoringStrategy.applyPoint(game, request.getTeamWinner());
        game.setScore(scoringStrategy.getScoreDisplay(game));

        PointEventDTO pointEvent = new PointEventDTO();
        pointEvent.setGameId(gameId);
        pointEvent.setSequenceNumber((game.getHistory()==null ?0:game.getHistory().size()) + 1);
        pointEvent.setWinningTeam(request.getTeamWinner());
        pointEvent.setScoreSnapshot(game.getScore());
        var mapped =pointEventMapper.toEntity(pointEvent);

        if (game.getHistory() == null) {
            game.setHistory(new ArrayList<>());
        }
        game.getHistory().add(pointEventMapper.toDto(pointEventRepository.save(mapped)));

        return   saveGame(game);

    }
    @Transactional
    public GameDTO recordMultiplePoints(Long gameId, List<PointRequest> points) {
        GameDTO result = null;
        for (PointRequest point : points) {
            result = recordPoint(gameId, point);
        }
        return result;
    }

    public GameDTO saveGame(GameDTO gameDTO) {
        GameEntity game = gameMapper.toEntity(gameDTO);
        var set =setRepository.findById(gameDTO.getSetId()).orElseThrow();

        game.setSet(set);
        GameEntity savedGame = gameRepository.save(game);

        scoreService.updateSetStatus(set.getId());

        return gameMapper.toDTO(savedGame);
    }


}
