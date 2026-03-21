package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ReferenceMapper.class})
public interface DiscountMapper {

    DiscountBasicDTO toBasicDTO(Discount discount);

    @Mapping(target = "events", ignore = true)
    DiscountDTO toDTO(Discount discount);
    
    @Mapping(target = "events", ignore = true)
    List<DiscountDTO> toDTOs(Collection<Discount> discounts);

    @Mapping(target = "discountID", ignore = true)
    @Mapping(target = "events", ignore = true)
    Discount toEntity(DiscountCreateDTO createDTO);

    @Mapping(target = "discountID", ignore = true)
    @Mapping(target = "events", ignore = true)
    void updateEntityFromBasicDto(DiscountBasicDTO dto, @MappingTarget Discount entity);
}