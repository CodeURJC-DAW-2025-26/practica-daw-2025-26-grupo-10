package es.tickethub.tickethub.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.repositories.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll() {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No events found");
        }
        return events;
    }

    public Event findById(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
    }

    public Event findByName(String name) {
        Optional<Event> optionalEvent = eventRepository.findByName(name);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event> findPaginated(int page, int size) {
        // Request for "give page X, size Y"
        Pageable pageable = PageRequest.of(page, size);
        // Repository returns a Page object; we use getContent to extract the list
        return eventRepository.findAll(pageable).getContent();
    }

    /**
     * Unified search method that supports optional filters for artist, category, and date.
     * Uses a Timestamp range to ensure full-day coverage for session searches.
     */
    public Page<Event> searchEvents(String artist, String category, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Timestamp start = null;
        Timestamp end = null;

        // If a date is provided, create a range from 00:00:00 to 23:59:59
        if (date != null) {
            start = Timestamp.valueOf(date.atStartOfDay());
            end = Timestamp.valueOf(date.atTime(23, 59, 59, 999999999));
        }

        // Convert empty strings to null so the SQL query ignores them
        String artParam = (artist != null && !artist.isBlank()) ? artist : null;
        String catParam = (category != null && !category.isBlank()) ? category : null;

        return eventRepository.findByFilters(artParam, catParam, start, end, pageable);
    }

    public boolean isEventActive(Long eventID) {
        Timestamp today = new Timestamp(System.currentTimeMillis());
        Optional<Event> eventOptional = eventRepository.findById(eventID);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            for (Session session : event.getSessions()) {
                if (session.getDate().after(today)) {
                    return true;
                }
            }
            return false;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with ID " + eventID + " does not exist.");
    }

    public List<String> getUniqueCategories () {
        return eventRepository.findAllUniqueCategories();
    }
}