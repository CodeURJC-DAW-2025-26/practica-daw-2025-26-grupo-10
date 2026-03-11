package es.tickethub.tickethub.dto;

import java.math.BigDecimal;
import java.util.List;

import es.tickethub.tickethub.entities.Ticket;

public record ZoneDTO(
    Long id,
    String name,
    Integer capacity,
    BigDecimal price,
    List<Ticket> tickets,
    Long event,
    boolean selected)
{}