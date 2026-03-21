package es.tickethub.tickethub.rest_controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.dto.TicketDTO;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.mappers.TicketMapper;
import es.tickethub.tickethub.services.TicketService;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketRestController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    public TicketRestController(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.findById(id);
        return ResponseEntity.ok(ticketMapper.toDTO(ticket));
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO dto) {
        Ticket ticket = ticketMapper.toDomain(dto);
        Ticket savedTicket = ticketService.save(ticket);
        return new ResponseEntity<>(ticketMapper.toDTO(savedTicket), HttpStatus.CREATED);
    }
}