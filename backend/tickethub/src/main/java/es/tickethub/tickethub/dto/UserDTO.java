package es.tickethub.tickethub.dto;

public record UserDTO(

    Long userID,
    String email,
    String username,
    String password,
    Boolean admin,
    Long version) 
{} 
