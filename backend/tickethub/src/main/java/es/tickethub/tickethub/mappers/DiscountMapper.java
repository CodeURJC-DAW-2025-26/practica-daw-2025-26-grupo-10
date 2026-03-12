package es.tickethub.tickethub.mappers;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.tickethub.tickethub.dto.DiscountDTO;
import es.tickethub.tickethub.entities.Discount;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    
    DiscountDTO toDTO(Discount discount);

    List <DiscountDTO> toDTOs(Collection <Discount> discounts);

    @Mapping(target = "discountID", ignore = true)
    Discount toDomain(DiscountDTO discountDTO);
}
