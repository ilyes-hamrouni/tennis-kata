package bforbank.tennis.mappers;

import bforbank.tennis.domains.dtos.PointEventDTO;
import bforbank.tennis.domains.entities.PointEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PointEventMapper {


    @Mapping(target = "gameId", source = "game.id")
    PointEventDTO toDto(PointEventEntity entity);

    @Mapping(target = "game.id", source="gameId")
    PointEventEntity toEntity(PointEventDTO dto);
}