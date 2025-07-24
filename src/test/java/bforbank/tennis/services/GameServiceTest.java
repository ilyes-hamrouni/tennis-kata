package bforbank.tennis.services;

import bforbank.tennis.domains.dtos.GameDTO;
import bforbank.tennis.domains.dtos.PointEventDTO;
import bforbank.tennis.domains.dtos.ScoringStrategy;
import bforbank.tennis.domains.entities.GameEntity;
import bforbank.tennis.domains.entities.PointEventEntity;
import bforbank.tennis.domains.entities.SetEntity;
import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import bforbank.tennis.domains.requests.PointRequest;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.mappers.GameMapper;
import bforbank.tennis.mappers.PointEventMapper;
import bforbank.tennis.mappers.SetMapper;
import bforbank.tennis.repositories.GameRepository;
import bforbank.tennis.repositories.PlayerRepository;
import bforbank.tennis.repositories.PointEventRepository;
import bforbank.tennis.repositories.SetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock private GameRepository gameRepository;
    @Mock private PointEventRepository pointEventRepository;
    @Mock private SetRepository setRepository;
    @Mock private GameMapper gameMapper;
    @Mock private PointEventMapper pointEventMapper;
    @Mock private ScoringStrategy scoringStrategy;
    @Mock private ScoreService scoreService;

    @InjectMocks private GameService gameService;

    private GameEntity gameEntity;
    private GameDTO gameDTO;
    private SetEntity setEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        setEntity = new SetEntity();
        setEntity.setId(1L);

        gameEntity = new GameEntity();
        gameEntity.setId(1L);
        gameEntity.setStatus(GameStatus.IN_PROGRESS);
        gameEntity.setSet(setEntity);

        gameDTO = new GameDTO();
        gameDTO.setId(1L);
        gameDTO.setStatus(GameStatus.IN_PROGRESS);
        gameDTO.setSetId(1L);
        gameDTO.setHistory(new ArrayList<>());
    }

    @Test
    void testCreateGame() {
        when(setRepository.findById(1L)).thenReturn(Optional.of(setEntity));
        when(gameRepository.save(any(GameEntity.class))).thenReturn(gameEntity);
        when(gameMapper.toDTO(any(GameEntity.class))).thenReturn(gameDTO);

        GameDTO result = gameService.createGame(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetGame_success() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(gameEntity));
        when(gameMapper.toDTO(gameEntity)).thenReturn(gameDTO);

        GameDTO result = gameService.getGame(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetGame_notFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TennisException.class, () -> gameService.getGame(1L));
    }

    @Test
    void testRecordPoint() {
        PointRequest pointRequest = new PointRequest();
        pointRequest.setTeamWinner(TeamType.TEAM_A);

        PointEventDTO pointEventDTO = new PointEventDTO();
        pointEventDTO.setGameId(1L);
        pointEventDTO.setSequenceNumber(1);
        pointEventDTO.setWinningTeam(TeamType.TEAM_A);
        pointEventDTO.setScoreSnapshot(null);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(gameEntity));
        when(gameMapper.toDTO(any(GameEntity.class))).thenReturn(gameDTO);
        when(pointEventMapper.toEntity(any())).thenReturn(new PointEventEntity());
        when(pointEventRepository.save(any())).thenReturn(new PointEventEntity());
        when(pointEventMapper.toDto(any())).thenReturn(pointEventDTO);
        when(gameMapper.toEntity(any())).thenReturn(gameEntity);
        when(setRepository.findById(any())).thenReturn(Optional.of(setEntity));
        when(gameRepository.save(any())).thenReturn(gameEntity);
        when(gameMapper.toDTO(any(GameEntity.class))).thenReturn(gameDTO);

        GameDTO result = gameService.recordPoint(1L, pointRequest);

        assertNotNull(result);
        verify(scoringStrategy).applyPoint(gameDTO, TeamType.TEAM_A);
        verify(scoreService).updateSetStatus(1L);
    }
}
