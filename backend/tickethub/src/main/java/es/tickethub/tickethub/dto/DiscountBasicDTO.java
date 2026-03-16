package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

public record DiscountBasicDTO(
    Long discountID, 
    String discountName, 
    BigDecimal amount, 
    Boolean percentage
) {}
