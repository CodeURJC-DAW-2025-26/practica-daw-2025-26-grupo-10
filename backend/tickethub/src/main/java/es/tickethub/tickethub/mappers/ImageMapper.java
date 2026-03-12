package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.tickethub.tickethub.dto.ImageDTO;
import es.tickethub.tickethub.entities.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    
    ImageDTO toDTO(Image image);

    List <ImageDTO> toDTOs(Collection <Image> images);

    @Mapping(target = "imageID", ignore = true)
    Image toDomain(ImageDTO imageDTO);

}
