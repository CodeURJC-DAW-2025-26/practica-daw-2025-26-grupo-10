package es.tickethub.tickethub.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    public List<User> getUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @Transactional
    public User updateUser(Long id, User userData) {
        User existingUser = getUserById(id);
        
        if (!existingUser.getEmail().equals(userData.getEmail()) &&
            userRepository.findByEmail(userData.getEmail()).isPresent()) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.CONFLICT, "El email ya está en uso");
        }

        existingUser.setUsername(userData.getUsername());
        existingUser.setEmail(userData.getEmail());
        existingUser.setAdmin(userData.getAdmin());
        
        return userRepository.save(existingUser);
    }
}