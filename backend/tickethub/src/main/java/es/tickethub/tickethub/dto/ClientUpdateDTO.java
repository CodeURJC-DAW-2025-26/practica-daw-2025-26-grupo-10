package es.tickethub.tickethub.dto;

public record ClientUpdateDTO(
    String email,
    String username,
    String name,
    String surname,
    Integer age,
    Integer phone,
    ImageBasicDTO profileImage
) {}