package es.tickethub.tickethub.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PurchaseCreateDTO(
    @NotNull(message = "El ID de la sesión es obligatorio")
    Long sessionID,

    @NotEmpty(message = "Debes seleccionar al menos un ticket para realizar la compra")
    List<TicketSelectionDTO> selections
) {}