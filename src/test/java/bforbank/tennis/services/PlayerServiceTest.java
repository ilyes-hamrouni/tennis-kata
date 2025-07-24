package bforbank.tennis.services;

import bforbank.tennis.domains.dtos.PlayerDTO;
import bforbank.tennis.domains.entities.PlayerEntity;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.mappers.PlayerMapper;
import bforbank.tennis.repositories.PlayerRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static bforbank.tennis.exceptions.TennisErrorConstants.*;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    private PlayerMapper playerMapper;
    private PlayerRepository playerRepository;
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerMapper = mock(PlayerMapper.class);
        playerRepository = mock(PlayerRepository.class);
        playerService = new PlayerService(playerMapper, playerRepository);
    }

    @Test
    void create_shouldSaveAndReturnPlayerDTO_whenValid() {
        PlayerDTO inputDto = Instancio.of(PlayerDTO.class)
                .set(field("name"), "Nadal")
                .create();

        PlayerEntity entity = Instancio.create(PlayerEntity.class);
        PlayerEntity savedEntity = Instancio.create(PlayerEntity.class);
        PlayerDTO outputDto = Instancio.create(PlayerDTO.class);

        when(playerMapper.toEntity(inputDto)).thenReturn(entity);
        when(playerRepository.save(entity)).thenReturn(savedEntity);
        when(playerMapper.toDTO(savedEntity)).thenReturn(outputDto);

        PlayerDTO result = playerService.create(inputDto);

        assertEquals(outputDto, result);
        verify(playerRepository).save(entity);
    }

    @Test
    void create_shouldThrow_whenNameIsNull() {
        PlayerDTO dto = Instancio.of(PlayerDTO.class)
                .set(field("name"), null)
                .create();

        TennisException ex = assertThrows(TennisException.class, () -> playerService.create(dto));
        assertEquals(400, ex.getErrorCode());
        assertEquals(PLAYER_NAME_REQUIRED, ex.getErrorMessage());
    }

    @Test
    void create_shouldThrow_whenNameIsEmpty() {
        PlayerDTO dto = Instancio.of(PlayerDTO.class)
                .set(field("name"), "   ")
                .create();

        TennisException ex = assertThrows(TennisException.class, () -> playerService.create(dto));
        assertEquals(400, ex.getErrorCode());
        assertEquals(PLAYER_NAME_REQUIRED, ex.getErrorMessage());
    }

    @Test
    void findAll_shouldReturnListOfDTOs() {
        List<PlayerEntity> entities = Instancio.ofList(PlayerEntity.class).size(2).create();
        List<PlayerDTO> dtos = Instancio.ofList(PlayerDTO.class).size(2).create();

        when(playerRepository.findAll()).thenReturn(entities);
        when(playerMapper.toDTO(any(PlayerEntity.class)))
                .thenReturn(dtos.get(0), dtos.get(1));

        List<PlayerDTO> result = playerService.findAll();

        assertEquals(2, result.size());
        verify(playerRepository).findAll();
    }

    @Test
    void findById_shouldReturnPlayerDTO_whenExists() {
        Long id = 1L;
        PlayerEntity entity = Instancio.create(PlayerEntity.class);
        PlayerDTO dto = Instancio.create(PlayerDTO.class);

        when(playerRepository.findById(id)).thenReturn(Optional.of(entity));
        when(playerMapper.toDTO(entity)).thenReturn(dto);

        PlayerDTO result = playerService.findById(id);

        assertEquals(dto, result);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        Long id = 123L;
        when(playerRepository.findById(id)).thenReturn(Optional.empty());

        TennisException ex = assertThrows(TennisException.class, () -> playerService.findById(id));
        assertEquals(404, ex.getErrorCode());
        assertEquals(PLAYER_NOT_FOUND, ex.getErrorMessage());
    }

    @Test
    void delete_shouldDelete_whenExists() {
        Long id = 5L;
        when(playerRepository.existsById(id)).thenReturn(true);

        playerService.delete(id);

        verify(playerRepository).deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        Long id = 99L;
        when(playerRepository.existsById(id)).thenReturn(false);

        TennisException ex = assertThrows(TennisException.class, () -> playerService.delete(id));
        assertEquals(404, ex.getErrorCode());
        assertEquals(PLAYER_NOT_FOUND, ex.getErrorMessage());
    }
}
