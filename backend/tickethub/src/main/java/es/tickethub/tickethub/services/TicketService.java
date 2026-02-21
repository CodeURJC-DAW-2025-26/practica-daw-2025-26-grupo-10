package es.tickethub.tickethub.services;
import org.springframework.beans.factory.annotation.Autowired;

import es.tickethub.tickethub.repositories.TicketRepository;

public class TicketService {
    
    @Autowired TicketRepository ticketRepository;

    
}
