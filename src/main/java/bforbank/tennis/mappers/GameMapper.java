package bforbank.tennis.mappers;


import bforbank.tennis.domains.dtos.GameDTO;
import bforbank.tennis.domains.dtos.PointEventDTO;
import bforbank.tennis.domains.entities.GameEntity;
import bforbank.tennis.domains.entities.PointEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PointEventMapper.class})
public interface GameMapper {

    @Mapping(target = "set.id", source = "setId")
    GameEntity toEntity(GameDTO dto);

    @Mapping(target = "setId", source = "set.id")
    @Mapping(target = "score", ignore = true)
    @Mapping(source = "history", target = "history")
    GameDTO toDTO(GameEntity entity);
    List<PointEventDTO> toDTO(List<PointEventEntity> history);

}
