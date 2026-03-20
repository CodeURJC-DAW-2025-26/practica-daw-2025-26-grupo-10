package es.tickethub.tickethub.mappers;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.ArtistBasicDTO;
import es.tickethub.tickethub.dto.ArtistCreateDTO;
import es.tickethub.tickethub.dto.ArtistDTO;
import es.tickethub.tickethub.dto.ArtistUpdateDTO;
import es.tickethub.tickethub.entities.Artist;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ReferenceMapper.class})
public interface ArtistMapper {

    @Mapping(target = "eventsIncoming", ignore = true)
    @Mapping(target = "lastEvents", ignore = true)
    @Mapping(target = "artistID", ignore = true)
    Artist toEntity(ArtistCreateDTO dto);

    @Mapping(target = "eventsIncoming", ignore = true)
    @Mapping(target = "lastEvents", ignore = true)
    void updateEntityFromDto(ArtistUpdateDTO dto, @MappingTarget Artist artist);

    ArtistDTO toDTO(Artist artist);
    ArtistBasicDTO toBasicDTO(Artist artist);
}