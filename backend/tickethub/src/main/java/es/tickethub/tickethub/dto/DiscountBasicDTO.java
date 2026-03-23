package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DiscountBasicDTO(

    @NotBlank(message = "El nombre del descuento es obligatorio") 
    String discountName, 

    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser positivo")
    BigDecimal amount, 
    
    @NotNull(message = "Indica si es porcentaje") 
    Boolean percentage
) {}
