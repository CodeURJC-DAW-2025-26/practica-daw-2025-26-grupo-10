package es.tickethub.tickethub.dto;

import java.math.BigDecimal;
import java.util.List;
import es.tickethub.tickethub.entities.Event;

public record DiscountDTO(

    Long discountID,
    String discountName,
    Boolean percentage,
    BigDecimal ammount,
    List<Event> events,
    boolean selected) 
{} 