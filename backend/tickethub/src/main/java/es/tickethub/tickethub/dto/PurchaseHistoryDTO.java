package es.tickethub.tickethub.dto;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseHistoryDTO(
        Long purchaseID,
        List<TicketBasicDTO> tickets,
        SessionHistoryDTO session,
        BigDecimal totalPrice) {
}
