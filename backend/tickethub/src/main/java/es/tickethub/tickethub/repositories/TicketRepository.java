package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
}
