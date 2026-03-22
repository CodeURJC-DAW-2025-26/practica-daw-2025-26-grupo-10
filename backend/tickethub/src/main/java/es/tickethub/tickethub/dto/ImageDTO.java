package es.tickethub.tickethub.dto;

import java.sql.Blob;

public record ImageDTO(
    Long imageID,
    String imageName,
    Blob imageCode,
    boolean first
)
{} 
