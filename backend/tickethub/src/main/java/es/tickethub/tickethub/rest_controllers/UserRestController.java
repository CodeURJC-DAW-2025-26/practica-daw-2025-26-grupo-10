package es.tickethub.tickethub.rest_controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.TicketDTO;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.mappers.TicketMapper;
import es.tickethub.tickethub.services.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {

    private final UserService userService;
    private final TicketMapper ticketMapper;

    public UserRestController(UserService userService, TicketMapper ticketMapper) {
        this.userService = userService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getTickets(HttpSession session) {
        Long clientId = (Long) session.getAttribute("clientId");
        List<Ticket> tickets = userService.getTicketsByClientId(clientId);
        return ResponseEntity.ok(ticketMapper.toDTOs(tickets));
    }
}