package es.tickethub.tickethub.dto;

import java.util.List;

public record EventUpdateDTO(
    Long eventID,
    String name,
    String category,
    String place,
    Long artistId,
    Integer targetAge,
    Integer capacity,
    List<Long> discountIds,       // Only IDs because to add a discount you had to have created it beforehand
    List<ZoneBasicDTO> zones,    // You can create
    List<SessionBasicDTO> sessions // You can create
) {}
