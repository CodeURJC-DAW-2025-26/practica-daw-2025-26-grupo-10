package es.tickethub.tickethub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.repositories.AdminRepository;
import es.tickethub.tickethub.repositories.EventRepository;
import es.tickethub.tickethub.repositories.PurchaseRepository;
import es.tickethub.tickethub.repositories.TicketRepository;
import es.tickethub.tickethub.repositories.UserRepository;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    // ---------- DASHBOARD ----------

    public int eventsActive() {
        int active = 0;

        for (Event event : eventRepository.findAll()) {
            if (eventService.isEventActive(event.getEventID())) {
                active++;
            }
        }

        return active;
    }

    public long getNumberUsers() {
        return userRepository.count();
    }

    public long getNumberTickets() {
        return ticketRepository.count();
    }

    public long getNumberAdmins() {
        return adminRepository.count();
    }

    // ---------- STATISTICS ----------

    public List<Object[]> getMonthEventData() {
        return purchaseRepository.getTicketsByMonthAndEvent();
    }

    public List<Object> getRankingLabels() {
        return purchaseRepository.getRankingByEvent()
                .stream()
                .map(d -> d[0])
                .toList();
    }

    public List<Object> getRankingValues() {
        return purchaseRepository.getRankingByEvent()
                .stream()
                .map(d -> d[1])
                .toList();
    }

    public List<Object> getEvolutionLabels() {
        return purchaseRepository.getTotalTicketsEvolution()
                .stream()
                .map(d -> d[0])
                .toList();
    }

    public List<Object> getEvolutionValues() {
        return purchaseRepository.getTotalTicketsEvolution()
                .stream()
                .map(d -> d[1])
                .toList();
    }
}

