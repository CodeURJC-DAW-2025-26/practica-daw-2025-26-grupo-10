package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiscountMapper {
    @Mapping(source = "amount", target = "amount")
    DiscountBasicDTO toBasicDTO(Discount discount);

    @Mapping(source = "amount", target = "amount")
    @Mapping(target = "events", ignore = true)
    DiscountDTO toDTO(Discount discount);
    
    @Mapping(source = "amount", target = "amount")
    @Mapping(target = "events", ignore = true)
    List<DiscountDTO> toDTOs(Collection<Discount> discounts);

    @Mapping(source = "amount", target = "amount")
    Discount toEntity(DiscountDTO discountDTO);

}