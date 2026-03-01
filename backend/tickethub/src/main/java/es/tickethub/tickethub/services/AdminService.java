package es.tickethub.tickethub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.repositories.AdminRepository;
import es.tickethub.tickethub.repositories.EventRepository;
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

    public int eventsActive() {
        int active = 0;

        for (Event event : eventRepository.findAll()) {
            if (eventService.isEventActive(event.getEventID())){
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
}
