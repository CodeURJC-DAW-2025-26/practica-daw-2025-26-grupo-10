package es.tickethub.tickethub.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.entities.User;
import es.tickethub.tickethub.repositories.UserRepository;
import lombok.Getter;

@Service
@Getter
public class UserService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserRepository userRepository;

    public Client getClientFromSession(Long clientId) {
        return clientService.getClientById(clientId);
    }

    public List<Ticket> getTicketsByClientId(Long clientId) {
        Client client = clientService.getClientById(clientId);
        List<Ticket> tickets = new ArrayList<>();

        for (Purchase purchase : client.getPurchases()) {
            tickets.addAll(purchase.getTickets());
        }

        return tickets;
    }

    public List<Map<String, Object>> getEventsInfoByClientId(Long clientId) {
        Client client = clientService.getClientById(clientId);
        List<Map<String, Object>> events = new ArrayList<>();

        for (Purchase purchase : client.getPurchases()) {
            Session eventSession = purchase.getSession();

            Map<String, Object> eventsInfo = new HashMap<>();
            eventsInfo.put("event", eventSession.getEvent().getName());
            eventsInfo.put("sessionDate", eventSession.getDate());

            events.add(eventsInfo);
        }

        return events;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isAdmin(User user) {
        return ((user != null) && (user.getAdmin() != null) && (user.getAdmin()));
    }
}