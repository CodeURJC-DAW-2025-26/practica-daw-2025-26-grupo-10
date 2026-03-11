package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

public record TicketDTO(
    Long ticketID,
    String code,
    Long zone,
    Long purchase,
    BigDecimal ticketPrice,
    Boolean isActive)
{}