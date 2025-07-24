package bforbank.tennis.services;

import bforbank.tennis.exceptions.TennisErrorConstants;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.mappers.PlayerMapper;
import bforbank.tennis.domains.dtos.PlayerDTO;
import bforbank.tennis.domains.entities.PlayerEntity;
import bforbank.tennis.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static bforbank.tennis.exceptions.TennisErrorConstants.ERROR_PLAYER_NOT_FOUND;

@Service
public class PlayerService {

    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerMapper playerMapper, PlayerRepository playerRepository) {
        this.playerMapper = playerMapper;
        this.playerRepository = playerRepository;
    }


    public PlayerDTO create(PlayerDTO playerDTO) {
        this.validatePlayerDTO(playerDTO);
        PlayerEntity entity = playerMapper.toEntity(playerDTO);
        PlayerEntity saved = playerRepository.save(entity);
        return playerMapper.toDTO(saved);
    }

    public List<PlayerDTO> findAll() {
        return playerRepository.findAll()
                .stream()
                .map(playerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PlayerDTO findById(Long id) {
        PlayerEntity entity = playerRepository.findById(id)
                .orElseThrow(() -> new TennisException(404, ERROR_PLAYER_NOT_FOUND));
        return playerMapper.toDTO(entity);
    }

    public void delete(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new TennisException(404, ERROR_PLAYER_NOT_FOUND);
        }
        playerRepository.deleteById(id);
    }

    private void validatePlayerDTO(PlayerDTO playerDTO) {
        if (playerDTO.getName() == null || playerDTO.getName().trim().isEmpty()) {
            throw new TennisException(
                    400,
                    TennisErrorConstants.ERROR_PLAYER_NAME_REQUIRED
            );
        }
    }
}
