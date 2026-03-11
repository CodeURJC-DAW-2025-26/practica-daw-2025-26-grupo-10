package es.tickethub.tickethub.dto;

public record AdminDTO(

    Long userID,
    String email,
    String username,
    String password,
    Boolean admin,
    Long version) 
{} 
