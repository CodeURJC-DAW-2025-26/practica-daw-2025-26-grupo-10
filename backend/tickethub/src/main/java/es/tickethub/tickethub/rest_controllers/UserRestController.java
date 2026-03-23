package es.tickethub.tickethub.rest_controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.dto.TicketDTO;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.entities.User;
import es.tickethub.tickethub.mappers.TicketMapper;
import es.tickethub.tickethub.services.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketMapper ticketMapper;

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getTickets(Principal principal) {

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Debes estar autenticado");
        }

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado en el sistema"));

       
        List<Ticket> tickets = userService.getTicketsByClientId(user.getId());

   
        return ResponseEntity.ok(ticketMapper.toDTOs(tickets));
    }
}