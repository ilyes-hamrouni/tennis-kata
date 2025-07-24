package bforbank.tennis.services;


import bforbank.tennis.domains.dtos.*;
import bforbank.tennis.domains.entities.GameEntity;
import bforbank.tennis.domains.entities.PlayerEntity;
import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import bforbank.tennis.domains.requests.CreateGameRequest;
import bforbank.tennis.domains.requests.PointRequest;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.mappers.GameMapper;
import bforbank.tennis.mappers.PointEventMapper;
import bforbank.tennis.repositories.GameRepository;
import bforbank.tennis.repositories.PlayerRepository;
import bforbank.tennis.repositories.PointEventRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;
    private PointEventRepository pointEventRepository;
    private PlayerRepository playerRepository;
    private GameMapper gameMapper;
    private PointEventMapper pointEventMapper;
    private ScoringStrategy scoringStrategy;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        pointEventRepository = mock(PointEventRepository.class);
        playerRepository = mock(PlayerRepository.class);
        gameMapper = mock(GameMapper.class);
        pointEventMapper = mock(PointEventMapper.class);
        scoringStrategy = mock(ScoringStrategy.class);

        gameService = new GameService(
                gameRepository,
                pointEventRepository,
                playerRepository,
                gameMapper,
                pointEventMapper,
                scoringStrategy
        );
    }

    @Test
    void createGame_shouldCreateGame_whenPlayersExist() {
        PlayerEntity playerA = Instancio.create(PlayerEntity.class);
        PlayerEntity playerB = Instancio.create(PlayerEntity.class);
        CreateGameRequest request = new CreateGameRequest();
        request.setTeamBIds(List.of(1L));
        request.setTeamAIds(List.of(2L));


        when(playerRepository.findById(1L)).thenReturn(Optional.of(playerA));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(playerB));

        GameEntity savedGame = new GameEntity();
        savedGame.setTeamA(List.of(playerA));
        savedGame.setTeamB(List.of(playerB));
        savedGame.setStatus(GameStatus.IN_PROGRESS);

        when(gameRepository.save(any())).thenReturn(savedGame);
        GameDTO gameDTO = Instancio.create(GameDTO.class);
        when(gameMapper.toDTO(savedGame)).thenReturn(gameDTO);

        GameDTO result = gameService.createGame(request);

        assertEquals(gameDTO, result);
        verify(gameRepository).save(any());
    }

    @Test
    void createGame_shouldThrow_whenPlayerNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        CreateGameRequest request = new CreateGameRequest();
        request.setTeamBIds(List.of(1L));
        request.setTeamAIds(List.of());

        assertThrows(TennisException.class, () -> gameService.createGame(request));
    }

    @Test
    void getGame_shouldReturnGameDTO_whenGameExists() {
        GameEntity entity = Instancio.create(GameEntity.class);
        GameDTO dto = Instancio.create(GameDTO.class);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(gameMapper.toDTO(entity)).thenReturn(dto);

        GameDTO result = gameService.getGame(1L);

        assertEquals(dto, result);
    }

    @Test
    void getGame_shouldThrow_whenGameNotFound() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TennisException.class, () -> gameService.getGame(99L));
    }

    @Test
    void recordPoint_shouldUpdateGameStateAndReturnUpdatedGame() {
        Long gameId = 10L;
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(gameId);

        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(gameId);
        gameDTO.setHistory(new ArrayList<>());

        PointRequest request = new PointRequest();
        request.setTeamWinner(TeamType.TEAM_A);

        PointEventDTO pointEventDTO = new PointEventDTO();
        pointEventDTO.setGameId(gameId);
        pointEventDTO.setWinningTeam(TeamType.TEAM_B);

        var pointEntity = Instancio.create(bforbank.tennis.domains.entities.PointEventEntity.class);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameEntity));
        when(gameMapper.toDTO(gameEntity)).thenReturn(gameDTO);
        when(pointEventMapper.toEntity(any())).thenReturn(pointEntity);
        when(pointEventRepository.save(pointEntity)).thenReturn(pointEntity);
        when(pointEventMapper.toDto(pointEntity)).thenReturn(pointEventDTO);
        when(gameMapper.toEntity(any())).thenReturn(gameEntity);
        when(gameRepository.save(any())).thenReturn(gameEntity);
        when(gameMapper.toDTO(gameEntity)).thenReturn(gameDTO);

        GameDTO result = gameService.recordPoint(gameId, request);

        assertEquals(gameDTO, result);
        verify(scoringStrategy).applyPoint(gameDTO, TeamType.TEAM_A);
        verify(scoringStrategy).getScoreDisplay(gameDTO);
    }
}
