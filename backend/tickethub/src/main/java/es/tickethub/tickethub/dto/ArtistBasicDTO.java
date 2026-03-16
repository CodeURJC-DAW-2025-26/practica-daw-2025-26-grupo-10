package es.tickethub.tickethub.dto;

public record ArtistBasicDTO(
    Long artistID, 
    String artistName, 
    ImageBasicDTO artistImage
) {}
