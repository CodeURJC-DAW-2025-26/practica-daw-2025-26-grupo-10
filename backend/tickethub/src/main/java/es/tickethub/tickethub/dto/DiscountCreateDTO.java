package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DiscountCreateDTO(
    @NotBlank(message = "El nombre del descuento es obligatorio") 
    String discountName,

    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser mayor que 0")
    BigDecimal amount,
    
    @NotNull(message = "Debes indicar si es porcentaje o valor fijo") 
    Boolean percentage
) {}
