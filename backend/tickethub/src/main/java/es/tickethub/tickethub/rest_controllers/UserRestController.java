package es.tickethub.tickethub.rest_controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.dto.TicketDTO;
import es.tickethub.tickethub.services.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getTickets(HttpSession session) {
        Long clientId = (Long) session.getAttribute("clientId");
        List<TicketDTO> tickets = userService.getTicketsDTOByClientId(clientId);
        return ResponseEntity.ok(tickets);
    }
}
