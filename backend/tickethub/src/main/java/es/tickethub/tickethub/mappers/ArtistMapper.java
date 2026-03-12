package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.tickethub.tickethub.dto.ArtistDTO;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Image;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    
    ArtistDTO toDTO(Artist artist);

    List <ArtistDTO> toDTOs(Collection <Artist> artists);

    @Mapping(target = "artistID", ignore = true)
    Artist toDomain(ArtistDTO artistDTO);

    default Long imageToLong(Image image) {
        return image == null ? null : image.getImageID();
    }

    default Image longToImage(Long id) {
        if (id == null) {
            return null;
        }
        Image img = new Image();
        img.setImageID(id);
        return img;
    }

}
