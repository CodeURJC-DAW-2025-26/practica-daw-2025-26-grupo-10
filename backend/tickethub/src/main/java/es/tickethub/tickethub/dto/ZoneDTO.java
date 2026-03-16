package es.tickethub.tickethub.dto;

import java.math.BigDecimal;
import java.util.List;

public record ZoneDTO(
    Long id,
    String name,
    Integer capacity,
    BigDecimal price,
    List<TicketBasicDTO> tickets,
    Long eventId,
    boolean selected
) {}