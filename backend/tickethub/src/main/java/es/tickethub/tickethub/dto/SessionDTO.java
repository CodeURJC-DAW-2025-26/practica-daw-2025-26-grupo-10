package es.tickethub.tickethub.dto;

import java.sql.Timestamp;
import java.util.List;

public record SessionDTO(

    Long sessionID,
    Long event,
    List<PurchaseBasicDTO> purchases,
    Timestamp date) 
{}