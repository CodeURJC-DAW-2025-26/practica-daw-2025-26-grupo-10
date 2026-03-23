package es.tickethub.tickethub.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.TicketDTO;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.mappers.TicketMapper;
import es.tickethub.tickethub.services.TicketService;

@RestController
@RequestMapping("/api/v1/admin/tickets")
public class TicketRestController {

    @Autowired private TicketService ticketService;
    @Autowired private TicketMapper ticketMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.findById(id);
        return ResponseEntity.ok(ticketMapper.toDTO(ticket));
    }
}