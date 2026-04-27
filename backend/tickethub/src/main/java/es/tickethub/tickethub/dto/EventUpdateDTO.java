package es.tickethub.tickethub.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EventUpdateDTO(

    @NotBlank(message = "El nombre es obligatorio")
    String name,

    @NotBlank(message = "La categoría es obligatoria")
    String category,

    @NotBlank(message = "El lugar es obligatorio")
    String place,

    @NotNull(message = "Debes asignar un artista")
    Long artistId,

    @NotNull @Positive(message = "La edad debe ser positiva")
    Integer targetAge,

    @Positive(message = "La capacidad debe ser positiva")
    Integer capacity,

    List<Long> discountIds,
    List<@Valid ZoneBasicDTO> zones, // @Valid to validate list elements
    List<SessionBasicDTO> sessions
) {}
