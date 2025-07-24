package bforbank.tennis.mappers;


import bforbank.tennis.domains.dtos.SetDTO;
import bforbank.tennis.domains.entities.SetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class})
public interface SetMapper {

    @Mapping(target = "matchId", source = "match.id")
    SetDTO toDTO(SetEntity entity);

    @Mapping(target = "match.id", source = "matchId")
    SetEntity toEntity(SetDTO dto);


}
