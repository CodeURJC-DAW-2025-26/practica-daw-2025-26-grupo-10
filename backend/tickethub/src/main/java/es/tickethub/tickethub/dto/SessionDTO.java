package es.tickethub.tickethub.dto;

import java.sql.Timestamp;
import java.util.List;

import es.tickethub.tickethub.entities.Purchase;

public record SessionDTO(

    Long sessionID,
    Long event,
    List<Purchase> purchases,
    Timestamp date) 
{}