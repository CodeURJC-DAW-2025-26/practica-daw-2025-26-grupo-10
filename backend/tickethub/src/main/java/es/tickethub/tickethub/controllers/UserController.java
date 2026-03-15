package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.services.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/change_password")
    public String changePassword() {
        return "user/change_password";
    }

    @GetMapping("/edit_profile")
    public String editProfile(HttpSession session, Model model) {
        Long clientId = (Long) session.getAttribute("clientId");
        Client client = userService.getClientFromSession(clientId);

        model.addAttribute("client", client);
        return "user/edit_profile";
    }

    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }

    @GetMapping("/tickets")
    public String showTickets(HttpSession clientSession, Model model) {
        Long clientId = (Long) clientSession.getAttribute("clientId");

        model.addAttribute("tickets", userService.getTicketsByClientId(clientId));
        model.addAttribute("events", userService.getEventsInfoByClientId(clientId));

        return "user/tickets";
    }
}