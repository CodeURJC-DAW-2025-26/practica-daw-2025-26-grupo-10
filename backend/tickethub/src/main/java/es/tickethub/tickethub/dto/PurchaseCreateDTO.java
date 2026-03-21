package es.tickethub.tickethub.dto;

import java.util.List;

public record PurchaseCreateDTO(
    Long sessionID,
    List<TicketSelectionDTO> selections
) {}