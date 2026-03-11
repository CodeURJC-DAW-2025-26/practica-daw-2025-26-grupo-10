package es.tickethub.tickethub.dto;

import java.math.BigDecimal;
import java.util.List;

import es.tickethub.tickethub.entities.Ticket;

public record PurchaseDTO(

    Long purchaseID,
    List<Ticket> tickets,
    Long session,
    Long client,
    BigDecimal totalPrice) 
{}