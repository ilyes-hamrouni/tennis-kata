package bforbank.tennis.mappers;


import bforbank.tennis.domains.dtos.MatchDTO;
import bforbank.tennis.domains.entities.MatchEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = { SetMapper.class })
public interface MatchMapper {
    MatchDTO toDTO(MatchEntity entity);
    MatchEntity toEntity(MatchDTO dto);
}