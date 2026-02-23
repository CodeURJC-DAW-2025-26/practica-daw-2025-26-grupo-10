package es.tickethub.tickethub.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.repositories.TicketRepository;

@Service
public class TicketService {
    
    @Autowired TicketRepository ticketRepository;

    
}
