package es.tickethub.tickethub.dto;

import java.util.List;

public record ArtistDTO(
    Long artistID,
    String artistName,
    String info,
    List<EventBasicDTO> eventsIncoming,
    List<EventBasicDTO> lastEvents,
    ImageBasicDTO artistImage,
    String instagram,
    String twitter,
    boolean selected
) {}