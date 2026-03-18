package es.tickethub.tickethub.services;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import es.tickethub.tickethub.dto.EventDTO;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.mappers.EventMapper;
import es.tickethub.tickethub.repositories.EventRepository;
import lombok.Getter;

@Service
@Getter
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private RecommendationService recommendationService;


    @Autowired
    private DiscountService discountService;

    @Autowired
    private EventMapper eventMapper;

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

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event findByName(String name) {
        Optional<Event> optionalEvent = eventRepository.findByName(name);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
    }

    public Event create(Event event, Long artistID, MultipartFile[] files) throws SQLException, IOException {
        Artist artist = artistService.findById(artistID);
        event.setArtist(artist);
        artist.getEventsIncoming().add(event);
        // checks if any file was uploaded
        if (files != null && files.length > 0) {
            event.getEventImages().addAll(convertFilesToImages(files));
        }
        // Calculate total capacity of the event by summing capacities of all zones
        // stream() to iterate over the zones
        event.setCapacity(calculateTotalCapacity(event.getZones()));

        return save(event);
    }

    public Event edit(Event oldEvent, Event editedEvent, Long artistID, List<Long> discountIDs, MultipartFile[] files)
            throws SQLException, IOException {

        oldEvent.setName(editedEvent.getName());
        oldEvent.setCapacity(calculateTotalCapacity(editedEvent.getZones()));
        oldEvent.setTargetAge(editedEvent.getTargetAge());

        Artist oldArtist = oldEvent.getArtist();
        Artist newArtist = artistService.findById(artistID);

        if (!oldArtist.getArtistID().equals(newArtist.getArtistID())) {
            oldArtist.getEventsIncoming().remove(oldEvent);
            oldArtist.getLastEvents().remove(oldEvent);

            newArtist.getEventsIncoming().add(oldEvent);
            oldEvent.setArtist(newArtist);
        }

        for (Zone zone : editedEvent.getZones()) {
            zone.setEvent(oldEvent);
            oldEvent.getZones().add(zone);
        }

        // Clear existing discounts
        for (Discount discount : new ArrayList<>(oldEvent.getDiscounts())) {
            discount.getEvents().remove(oldEvent);
        }
        oldEvent.getDiscounts().clear();

        // Add new discounts
        for (Long discountID : discountIDs) {
            Discount discount = discountService.findById(discountID);
            if (!discount.getEvents().contains(oldEvent)) {
                discount.getEvents().add(oldEvent);
                oldEvent.getDiscounts().add(discount);
            }
        }

        oldEvent.setPlace(editedEvent.getPlace());
        oldEvent.setCategory(editedEvent.getCategory());

        if (files != null && files.length > 0) {
            oldEvent.getEventImages().addAll(convertFilesToImages(files));
        }

        return save(oldEvent);
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
     * Unified search method that supports optional filters for artist, category,
     * and date.
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

    public List<String> getUniqueCategories() {
        return eventRepository.findAllUniqueCategories();
    }

    private List<Image> convertFilesToImages(MultipartFile[] files) throws SQLException, IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                images.add(new Image(file.getOriginalFilename(), new SerialBlob(file.getBytes())));
            }
        }
        return images;
    }

    private int calculateTotalCapacity(List<Zone> zones) {
        return zones.stream().mapToInt(Zone::getCapacity).sum();
    }
    // DELETE
    public void deleteEvent(Long eventID) {
        Event event = findByIdOrThrow(eventID);

        Artist artist = event.getArtist();

        if (artist != null) {
            artist.getLastEvents().remove(event);
            artist.getEventsIncoming().remove(event);
        }

        event.setArtist(null);
        event.setZones(null);

        eventRepository.delete(event);
    }
    
    // Top eventos por ventas
public List<Event> getTopSellingEvents(int limit) {
    List<Event> allEvents = findAll(); // tu método existente
    return allEvents.stream()
            .sorted((e1, e2) -> {
                int sales1 = (e1.getSessions() == null) ? 0
                        : e1.getSessions().stream()
                                .flatMap(s -> s.getPurchases().stream())
                                .mapToInt(p -> p.getTickets().size())
                                .sum();
                int sales2 = (e2.getSessions() == null) ? 0
                        : e2.getSessions().stream()
                                .flatMap(s -> s.getPurchases().stream())
                                .mapToInt(p -> p.getTickets().size())
                                .sum();
                return Integer.compare(sales2, sales1);
            })
            .limit(limit)
            .toList();
}

// Next Events
    public List<Event> getNextUpcomingEvents(int limit) {
        List<Event> allEvents = findAll();
        return allEvents.stream()
                .sorted(Comparator.comparing(event -> {
                    if (event.getSessions() == null || event.getSessions().isEmpty())
                        return Instant.MAX;
                    return event.getSessions().stream()
                            .map(session -> session.getDate().toInstant())
                            .filter(date -> !date.isBefore(Instant.now()))
                            .min(Instant::compareTo)
                            .orElse(Instant.MAX);
                }))
                .limit(limit)
                .toList();
    }

    public List<Event> getRecommendedEvents(Client client, ServerRecommendationService serverService, int limit) {
        ClientRecommendationService crs = new ClientRecommendationService(client);
        return recommendationService.recommendEvents(crs, serverService, limit, true);
    }

    // ======================
    // REST METHODS
    // ======================

    public EventDTO createEventREST(Event event) {
        Event createdEvent = save(event);
        return eventMapper.toDTO(createdEvent);
    }

    public Event findByIdOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));
    }

    public Page<Event> getFirstPageOfEvents() {
        return searchEvents(null, null, null, 0, 5);
    }
}