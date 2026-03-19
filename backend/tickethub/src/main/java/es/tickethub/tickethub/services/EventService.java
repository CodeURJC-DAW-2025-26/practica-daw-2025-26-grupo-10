package es.tickethub.tickethub.services;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.mappers.EventMapper;
import es.tickethub.tickethub.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private EventCreationService eventCreationService;

    @Autowired
    private ArtistService artistService;

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

    public Event findByIdOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));
    }

    public Event findByName(String name) {
        return eventRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }


    public Event create(Event event, Long artistID, MultipartFile[] files) {
        eventCreationService.prepareEventForCreation(event, artistID, files);
        return save(event);
    }

    public int calculateTotalCapacity(List<Zone> zones) {
        return zones.stream().mapToInt(Zone::getCapacity).sum();
    }

    public Event edit(Event oldEvent, Event editedEvent, Long artistID, List<Long> discountIDs, MultipartFile[] files)
            throws SQLException, IOException {

        oldEvent.setName(editedEvent.getName());
        oldEvent.setCapacity(calculateTotalCapacity(editedEvent.getZones()));
        oldEvent.setTargetAge(editedEvent.getTargetAge());
        oldEvent.setPlace(editedEvent.getPlace());
        oldEvent.setCategory(editedEvent.getCategory());

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

        for (Discount discount : new ArrayList<>(oldEvent.getDiscounts())) {
            discount.getEvents().remove(oldEvent);
        }
        oldEvent.getDiscounts().clear();

        for (Long discountID : discountIDs) {
            Discount discount = discountService.findById(discountID);
            if (!discount.getEvents().contains(oldEvent)) {
                discount.getEvents().add(oldEvent);
                oldEvent.getDiscounts().add(discount);
            }
        }

        if (files != null && files.length > 0) {
            oldEvent.getEventImages().addAll(imageService.createImagesFromFiles(files));
        }
        return save(oldEvent);
    }


    public List<Event> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAll(pageable).getContent();
    }

    public Page<Event> searchEvents(String artist, String category, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Timestamp start = null;
        Timestamp end = null;

        if (date != null) {
            start = Timestamp.valueOf(date.atStartOfDay());
            end = Timestamp.valueOf(date.atTime(23, 59, 59, 999999999));
        }

        String artParam = (artist != null && !artist.isBlank()) ? artist : null;
        String catParam = (category != null && !category.isBlank()) ? category : null;

        return eventRepository.findByFilters(artParam, catParam, start, end, pageable);
    }

    public boolean isEventActive(Long eventID) {
        Event event = findByIdOrThrow(eventID);
        Timestamp today = new Timestamp(System.currentTimeMillis());

        return event.getSessions().stream()
                .anyMatch(session -> session.getDate().after(today));
    }

    public List<String> getUniqueCategories() {
        return eventRepository.findAllUniqueCategories();
    }

    public void deleteEvent(Long eventID) {
        Event event = findByIdOrThrow(eventID);

        if (event.getArtist() != null) {
            event.getArtist().getEventsIncoming().remove(event);
            event.getArtist().getLastEvents().remove(event);
        }

        event.setArtist(null);
        event.setZones(null);

        eventRepository.delete(event);
    }

    public EventDTO createEventREST(Event event) {
        return eventMapper.toDTO(save(event));
    }

    public Page<Event> getEventsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAll(pageable);
    }

    public boolean existsById(Long eventID) {
        return eventRepository.existsById(eventID);
    }

    public Page<Event> getFirstPageOfEvents() {
        return searchEvents(null, null, null, 0, 5);
    }
}