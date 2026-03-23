package es.tickethub.tickethub.dto;

public record AdminDTO(
    Long userID,
    String email,
    String username,
    Boolean admin,
    Long version
) {}
