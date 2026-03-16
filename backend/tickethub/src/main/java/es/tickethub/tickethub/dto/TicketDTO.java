package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

public record TicketDTO(
    Long ticketID,
    String code,
    ZoneBasicDTO zone,
    PurchaseBasicDTO purchase,
    BigDecimal ticketPrice,
    Boolean isActive
) {}