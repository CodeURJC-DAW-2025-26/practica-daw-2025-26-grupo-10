package es.tickethub.tickethub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.dto.AdminDashboardDTO;
import es.tickethub.tickethub.dto.AdminStatisticsDTO;
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

    public AdminDashboardDTO getDashboard() {
        return new AdminDashboardDTO(
                eventsActive(),
                getNumberTickets(),
                getNumberUsers(),
                getNumberAdmins()
        );
    }

    // ---------- STATISTICS ----------

    public List<Object[]> getMonthEventData() {
        return purchaseRepository.getTicketsByMonthAndEvent();
    }

    public List<String> getRankingLabels() {
        return purchaseRepository.getRankingByEvent()
                .stream()
                .map(d -> String.valueOf(d[0]))
                .toList();
    }

    public List<Number> getRankingValues() {
        return purchaseRepository.getRankingByEvent()
                .stream()
                .map(d -> (Number) d[1])
                .toList();
    }

    public List<String> getEvolutionLabels() {
        return purchaseRepository.getTotalTicketsEvolution()
                .stream()
                .map(d -> String.valueOf(d[0]))
                .toList();
    }

    public List<Number> getEvolutionValues() {
        return purchaseRepository.getTotalTicketsEvolution()
                .stream()
                .map(d -> (Number) d[1])
                .toList();
    }

    public AdminStatisticsDTO getStatistics() {
        return new AdminStatisticsDTO(
                getMonthEventData(),
                getRankingLabels(),
                getRankingValues(),
                getEvolutionLabels(),
                getEvolutionValues()
        );
    }
}