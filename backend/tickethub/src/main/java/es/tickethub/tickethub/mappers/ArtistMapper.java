package es.tickethub.tickethub.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.ArtistBasicDTO;
import es.tickethub.tickethub.dto.ArtistDTO;
import es.tickethub.tickethub.entities.Artist;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE) 
public interface ArtistMapper {
    ArtistDTO toDTO(Artist artist);
    ArtistBasicDTO toBasicDTO(Artist artist);
    
    Artist pathDTO(ArtistDTO artistDTO, @MappingTarget Artist artist);
}