package es.tickethub.tickethub.mappers;

import es.tickethub.tickethub.dto.*;
import es.tickethub.tickethub.entities.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiscountMapper {

    DiscountDTO toDTO(Discount discount);
    DiscountBasicDTO toBasicDTO(Discount discount);
    List<DiscountDTO> toDTOs(Collection<Discount> discounts);

    @Mapping(target = "discountID", ignore = true)
    Discount toDomain(DiscountDTO discountDTO);

    default Discount fromBasic(DiscountBasicDTO basic) {
        if (basic == null) return null;
        Discount discount = new Discount();
        discount.setDiscountID(basic.discountID());
        return discount;
    }
}