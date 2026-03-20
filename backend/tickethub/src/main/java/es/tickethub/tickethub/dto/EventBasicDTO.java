package es.tickethub.tickethub.dto;

public record EventBasicDTO(
    Long eventID,
    String name,
    String category,
    String place,
    String mainImageUrl
) {}