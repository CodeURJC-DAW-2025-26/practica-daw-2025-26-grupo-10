package es.tickethub.tickethub.dto;

import java.math.BigDecimal;
import java.util.List;

import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.entities.Purchase;

public record ClientDTO (

    Long userID,
    String email,
    String username,
    String password,
    Boolean admin,
    Long version,
    List<Purchase> purchases,
    String name,
    String surname,
    Integer age,
    Integer phone,
    BigDecimal coins,
    List<String> subjects,
    Image profileImage) 
{}