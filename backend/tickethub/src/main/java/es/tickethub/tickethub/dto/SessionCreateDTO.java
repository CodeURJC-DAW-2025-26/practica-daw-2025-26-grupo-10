package es.tickethub.tickethub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SessionCreateDTO(
    @JsonProperty("eventId")
    @NotNull(message = "El ID del evento es obligatorio")
    Long eventID,
    
    @NotBlank(message = "La fecha de la sesión es obligatoria")
    String dateStr
) {}