package es.tickethub.tickethub.dto;

import java.util.List;

public record IndexResponseDTO(
    List<EventBasicDTO> eventsTop,
    List<EventBasicDTO> eventsBottom,
    List<ArtistBasicDTO> artists,
    List<EventBasicDTO> recommendedEvents
) {}