package es.tickethub.tickethub.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.TicketDTO;
import es.tickethub.tickethub.services.TicketService;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketRestController {
    @Autowired private TicketService ticketService;
    
    /**
     * Retrieves a single ticket by its unique identifier.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    /**
     * Creates a new ticket record from a DTO.
     */
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO dto) {
        return new ResponseEntity<>(ticketService.save(dto), HttpStatus.CREATED);
    }
}
