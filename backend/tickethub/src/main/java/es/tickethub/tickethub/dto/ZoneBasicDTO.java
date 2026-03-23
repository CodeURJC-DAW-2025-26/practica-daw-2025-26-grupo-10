package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ZoneBasicDTO(
    Long id, 

    @NotBlank(message = "El nombre de la zona es obligatorio") 
    String name, 

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser un número positivo")
    Integer capacity, 
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    BigDecimal price
) {}
