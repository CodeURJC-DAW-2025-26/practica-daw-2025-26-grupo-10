package es.tickethub.tickethub.dto;

import jakarta.validation.constraints.NotBlank;

public record SessionCreateDTO(
    
    @NotBlank(message = "La fecha de la sesión es obligatoria")
    String dateStr
) {}