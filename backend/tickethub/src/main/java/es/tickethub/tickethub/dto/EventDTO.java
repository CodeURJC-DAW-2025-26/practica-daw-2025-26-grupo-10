package es.tickethub.tickethub.dto;

import java.util.List;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.entities.Zone;

public record EventDTO (

    Long eventID,
    String name,
    Integer capacity,
    Integer targetAge,
    Artist artist,
    List<Session> sessions,
    List<Zone> zones,
    List<Discount> discounts,
    String place,
    String category,
    List<Image> eventImages,
    Image mainImage) 
{}
