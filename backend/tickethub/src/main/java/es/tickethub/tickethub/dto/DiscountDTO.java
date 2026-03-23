package es.tickethub.tickethub.dto;

import java.math.BigDecimal;
import java.util.List;

public record DiscountDTO(

    Long discountID,
    String discountName,
    Boolean percentage,
    BigDecimal amount,
    List<EventBasicDTO> events,
    boolean selected) 
{} 