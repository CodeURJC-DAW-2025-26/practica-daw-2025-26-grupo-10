package es.tickethub.tickethub.dto;

import java.util.List;

import es.tickethub.tickethub.entities.Event;

public record ArtistDTO(

    Long artistID,
    String artistName,
    String info,
    List<Event> eventsIncoming,
    List<Event> lastEvents,
    Long artistImage,
    String instagram,
    String twitter,
    boolean selected) 
{}