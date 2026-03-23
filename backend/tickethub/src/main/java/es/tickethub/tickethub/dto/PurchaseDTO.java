package es.tickethub.tickethub.dto;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseDTO(
    Long purchaseID,
    List<TicketBasicDTO> tickets,
    SessionBasicDTO session,
    Long clientId,
    BigDecimal totalPrice
) {}