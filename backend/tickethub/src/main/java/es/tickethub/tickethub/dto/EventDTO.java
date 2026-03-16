package es.tickethub.tickethub.dto;

import java.util.List;

public record EventDTO(
    Long eventID,
    String name,
    Integer capacity,
    Integer targetAge,
    ArtistBasicDTO artist,         
    List<SessionBasicDTO> sessions,
    List<ZoneBasicDTO> zones,
    List<DiscountBasicDTO> discounts, 
    String place,
    String category,
    List<ImageBasicDTO> eventImages,
    ImageBasicDTO mainImage
) {}