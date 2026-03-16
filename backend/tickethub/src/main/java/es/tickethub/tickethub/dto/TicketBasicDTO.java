package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

public record TicketBasicDTO(
    Long ticketID, 
    String code, 
    BigDecimal ticketPrice, 
    Boolean isActive
) {}
