package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.tickethub.tickethub.dto.ArtistBasicDTO;
import es.tickethub.tickethub.dto.ArtistDTO;
import es.tickethub.tickethub.entities.Artist;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtistMapper {

    ArtistDTO toDTO(Artist artist);
    
    ArtistBasicDTO toBasicDTO(Artist artist);

    List<ArtistDTO> toDTOs(Collection<Artist> artists);

    @Mapping(target = "artistID", ignore = true)
    Artist toDomain(ArtistDTO artistDTO);

    default Artist fromBasic(ArtistBasicDTO basic) {
        if (basic == null) return null;
        Artist artist = new Artist();
        artist.setArtistID(basic.artistID());
        return artist;
    }
}
