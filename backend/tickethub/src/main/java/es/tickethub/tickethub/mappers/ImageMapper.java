package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper {

    ImageDTO toDTO(Image image);
    ImageBasicDTO toBasicDTO(Image image);
    List<ImageDTO> toDTOs(Collection<Image> images);

    @Mapping(target = "imageID", ignore = true)
    Image toDomain(ImageDTO imageDTO);

    default Image fromBasic(ImageBasicDTO basic) {
        if (basic == null) return null;
        Image image = new Image();
        image.setImageID(basic.imageID());
        return image;
    }
}