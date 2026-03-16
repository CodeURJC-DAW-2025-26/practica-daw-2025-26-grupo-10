package es.tickethub.tickethub.dto;

import java.math.BigDecimal;

public record ZoneBasicDTO(
    Long id, 
    String name, 
    Integer capacity, 
    BigDecimal price
) {}
