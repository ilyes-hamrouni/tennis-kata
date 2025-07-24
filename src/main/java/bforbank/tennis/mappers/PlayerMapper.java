package bforbank.tennis.mappers;

import bforbank.tennis.domains.dtos.PlayerDTO;
import bforbank.tennis.domains.entities.PlayerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerDTO toDTO(PlayerEntity entity);
    PlayerEntity toEntity(PlayerDTO dto);

    List<PlayerDTO> toPlayerDTOList(List<PlayerEntity> entities);
    List<PlayerEntity> toPlayerEntityList(List<PlayerDTO> dtos);
}
