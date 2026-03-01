package es.tickethub.tickethub.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.services.ClientService;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.entities.Client;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/change_password")
    public String changePassword() {

        return "user/change_password";
    }

    @GetMapping("/edit_profile")
    public String editProfile(HttpSession session, Model model) {

        Long clientId = (Long) session.getAttribute("clientId");
        Client client = clientService.getClientById(clientId);

        model.addAttribute("client", client);

        return "user/edit_profile";
    }

    @GetMapping("/profile")
    public String profile() {

        return "user/profile";
    }

    @GetMapping("/tickets")
    public String showTickets(HttpSession clientSession, Model model) {
        /*
         * This must get the clientID from the clientSession information.
         * When we have the client we get the purchases from the client and for each
         * purchase we get the tickets
         * and the session. With the session we get the event and the date of the ticket
         * to show it at the html view
         */
        Long clientID = (Long) clientSession.getAttribute("clientId");
        Client client = clientService.getClientById(clientID);
        List<Purchase> purchases = client.getPurchases();
        List<Ticket> tickets = new ArrayList<>();

        List<Map<String, Object>> events = new ArrayList<>();

        for (Purchase purchase : purchases) {
            tickets.addAll(purchase.getTickets());
            Session eventSession = purchase.getSession();

            Map<String, Object> eventsInfo = new HashMap<>();

            eventsInfo.put("event", eventSession.getEvent().getName());
            eventsInfo.put("sessionDate", eventSession.getDate());

            events.add(eventsInfo);
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("events", events);

        return "user/tickets";
    }
}
