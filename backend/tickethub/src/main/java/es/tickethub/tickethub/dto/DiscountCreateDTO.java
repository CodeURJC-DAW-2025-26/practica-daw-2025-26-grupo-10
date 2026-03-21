package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DiscountCreateDTO(
    @NotBlank String discountName,
    @NotNull BigDecimal amount,
    @NotNull Boolean percentage
) {}
