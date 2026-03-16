package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

public record PurchaseBasicDTO(
    Long purchaseID, 
    BigDecimal totalPrice
) {}
