package es.tickethub.tickethub.dto;

public record UserDTO(
    Long userID,
    String email,
    String username,
    Boolean admin,
    Long version
) {}
