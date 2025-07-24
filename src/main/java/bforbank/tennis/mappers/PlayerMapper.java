package bforbank.tennis.mappers;

import bforbank.tennis.domains.dtos.PlayerDTO;
import bforbank.tennis.domains.entities.PlayerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerDTO toDTO(PlayerEntity entity);
    PlayerEntity toEntity(PlayerDTO dto);
}
