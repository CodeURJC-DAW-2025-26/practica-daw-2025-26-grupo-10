package es.tickethub.tickethub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EventCreateDTO(
    @NotBlank(message = "El nombre del evento es obligatorio")
    String name,

    @NotBlank(message = "La categoría es obligatoria")
    String category,

    @NotBlank(message = "El lugar es obligatorio")
    String place,

    @NotNull(message = "Debes seleccionar un artista")
    Long artistId,

    @NotNull(message = "La edad objetivo es obligatoria")
    @Positive(message = "La edad debe ser un número positivo")
    Integer targetAge
    
) {}