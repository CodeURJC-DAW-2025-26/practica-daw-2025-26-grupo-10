package es.tickethub.tickethub.dto;

import java.sql.Timestamp;

public record SessionBasicDTO(
    Long sessionID, 
    Timestamp date
) {}