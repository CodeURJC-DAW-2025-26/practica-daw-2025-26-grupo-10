package es.tickethub.tickethub.dto;

import java.util.List;

public record EventBasicDTO(
    Long eventID,
    String name,
    ArtistBasicDTO artist,
    String category,
    String place,
    List<SessionBasicDTO> sessions,
    String mainImageUrl
) {}