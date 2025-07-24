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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SetServiceTest {

    @Mock private SetRepository setRepository;
    @Mock private MatchRepository matchRepository;
    @Mock private SetMapper setMapper;
    @Mock private GameMapper gameMapper;
    @Mock private GameRepository gameRepository;

    @InjectMocks private SetService setService;

    private MatchEntity match;
    private SetEntity set;
    private SetDTO setDTO;
    private GameEntity gameEntity;
    private GameDTO gameDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        match = new MatchEntity();
        match.setId(1L);
        match.setTeamA(List.of());
        match.setTeamB(List.of());

        set = new SetEntity();
        set.setId(1L);
        set.setMatch(match);
        set.setStatus(GameStatus.IN_PROGRESS);

        setDTO = new SetDTO();
        setDTO.setId(1L);

        gameEntity = new GameEntity();
        gameEntity.setId(10L);

        gameDTO = new GameDTO();
        gameDTO.setId(10L);
    }

    @Test
    void testCreateSet() {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(setRepository.save(any(SetEntity.class))).thenReturn(set);
        when(setMapper.toDTO(set)).thenReturn(setDTO);

        SetDTO result = setService.createSet(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreateSet_matchNotFound() {
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TennisException.class, () -> setService.createSet(1L));
    }

    @Test
    void testRecordGameWin_teamA_winsSet() {
        set.setTeamAGamesWon(5);
        set.setTeamBGamesWon(3);
        when(setRepository.findById(1L)).thenReturn(Optional.of(set));

        setService.recordGameWin(1L, TeamType.TEAM_A);

        assertEquals(6, set.getTeamAGamesWon());
        assertEquals(GameStatus.FINISHED, set.getStatus());
        assertEquals(TeamType.TEAM_A, set.getWinner());
    }

    @Test
    void testCreateGameInSet_success() {
        when(setRepository.findById(1L)).thenReturn(Optional.of(set));
        when(gameRepository.save(any(GameEntity.class))).thenReturn(gameEntity);
        when(gameMapper.toDTO(gameEntity)).thenReturn(gameDTO);

        GameDTO result = setService.createGameInSet(1L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void testCreateGameInSet_setFinished() {
        set.setStatus(GameStatus.FINISHED);
        when(setRepository.findById(1L)).thenReturn(Optional.of(set));

        assertThrows(TennisException.class, () -> setService.createGameInSet(1L));
    }

    @Test
    void testGetSetsByMatch() {
        when(setRepository.findByMatchId(1L)).thenReturn(List.of(set));
        when(setMapper.toDTO(set)).thenReturn(setDTO);

        List<SetDTO> result = setService.getSetsByMatch(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetSetById_success() {
        when(setRepository.findById(1L)).thenReturn(Optional.of(set));
        when(setMapper.toDTO(set)).thenReturn(setDTO);

        SetDTO result = setService.getSetById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetSetById_notFound() {
        when(setRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TennisException.class, () -> setService.getSetById(1L));
    }

    @Test
    void testGetLatestGameInSet_success() {
        GameEntity anotherGame = new GameEntity();
        anotherGame.setId(9L);

        when(gameRepository.findBySetId(1L)).thenReturn(List.of(anotherGame, gameEntity));
        when(gameMapper.toDTO(gameEntity)).thenReturn(gameDTO);

        GameDTO result = setService.getLatestGameInSet(1L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void testGetLatestGameInSet_notFound() {
        when(gameRepository.findBySetId(1L)).thenReturn(Collections.emptyList());

        assertThrows(TennisException.class, () -> setService.getLatestGameInSet(1L));
    }
}

