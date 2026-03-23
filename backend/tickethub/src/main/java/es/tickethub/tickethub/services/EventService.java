package es.tickethub.tickethub.services;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.dto.EventDTO;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.mappers.EventMapper;
import es.tickethub.tickethub.repositories.EventRepository;
import es.tickethub.tickethub.services.EventServices.EventCreationService;
import es.tickethub.tickethub.services.EventServices.EventRelationService;

/**
 * Service class responsible for managing events.
 * Provides methods to create, edit, delete, search, and paginate events.
 */
@Service
public class EventService {

    @Autowired
    @Lazy
    private PurchaseService purchaseService; // Inyecta el SERVICIO
    @Autowired
    private EventRelationService eventRelationService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private EventCreationService eventCreationService;

    @Autowired
    private EventMapper eventMapper;

    /**
     * Constructor for EventService with repository injection.
     *
     * @param eventRepository Repository for Event entities
     */
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieves all events.
     *
     * @return List of events
     * @throws ResponseStatusException If no events are found
     */
    public List<Event> findAll() {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No events found");
        }
        return events;
    }

    /**
     * Finds an event by ID or throws a 404 exception if not found.
     *
     * @param id Event ID
     * @return Event entity
     * @throws ResponseStatusException If event is not found
     */
    public Event findByIdOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));
    }

    /**
     * Finds an event by name.
     *
     * @param name Event name
     * @return Event entity
     * @throws ResponseStatusException If event is not found
     */
    public Event findByName(String name) {
        return eventRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    /**
     * Saves or updates an event.
     *
     * @param event Event entity
     * @return Saved Event entity
     */
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    /**
     * Deletes an event by its ID.
     *
     * @param id Event ID
     */
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    /**
     * Creates a new event with associated artist and images.
     *
     * @param event    Event entity to create
     * @param artistID ID of the associated artist
     * @param files    Array of image files to attach
     * @return Created Event entity
     */
    public Event create(Event event, Long artistID, MultipartFile[] files) {
        eventCreationService.prepareEventForCreation(event, artistID, files);
        return save(event);
    }

    /**
     * Calculates the total capacity of a list of zones.
     *
     * @param zones List of zones
     * @return Total capacity
     */
    public int calculateTotalCapacity(List<Zone> zones) {
        return zones.stream().mapToInt(Zone::getCapacity).sum();
    }

    /**
     * Edits an existing event with new details, artist, discounts, and images.
     *
     * @param oldEvent    Original Event entity
     * @param editedEvent Edited Event entity
     * @param artistID    ID of the associated artist
     * @param discountIDs List of discount IDs
     * @param files       Array of image files
     * @return Updated Event entity
     * @throws SQLException If database update fails
     * @throws IOException  If image processing fails
     */
    public Event edit(Event oldEvent, Event editedEvent, Long artistID, List<Long> discountIDs, MultipartFile[] files)
            throws SQLException, IOException {
        eventRelationService.updateArtist(oldEvent, artistID);
        eventRelationService.addZones(oldEvent, editedEvent.getZones());
        eventRelationService.syncDiscounts(oldEvent, discountIDs);
        imageService.addImagesToEvent(oldEvent, files);
        return save(oldEvent);
    }

    /**
     * Deletes a specific image from an event.
     *
     * @param eventID Event ID
     * @param imageID Image ID
     */
    public void deleteEventImage(Long eventID, Long imageID) {
        Event event = findByIdOrThrow(eventID);
        imageService.deleteImageFromEvent(event, imageID);
    }

    /**
     * Returns a paginated list of events.
     *
     * @param page Page number
     * @param size Page size
     * @return List of events for the requested page
     */
    public List<Event> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAll(pageable).getContent();
    }

    /**
     * Searches events by optional filters: artist, category, and date.
     *
     * @param artist   Artist name filter
     * @param category Category filter
     * @param date     Date filter
     * @param page     Page number
     * @param size     Page size
     * @return Page of filtered events
     */
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

    /**
     * Checks if an event has any upcoming sessions.
     *
     * @param eventID Event ID
     * @return True if the event has future sessions
     */
    public boolean isEventActive(Long eventID) {
        Event event = findByIdOrThrow(eventID);
        Timestamp today = new Timestamp(System.currentTimeMillis());

        return event.getSessions().stream()
                .anyMatch(session -> session.getDate().after(today));
    }

    /**
     * Returns a list of unique categories across all events.
     *
     * @return List of category names
     */
    public List<String> getUniqueCategories() {
        return eventRepository.findAllUniqueCategories();
    }

    /**
     * Deletes an event and removes all relationships with artists, zones, and
     * discounts.
     *
     * @param eventID Event ID
     */
    @Transactional
    public void deleteEvent(Long eventID) {
        Event event = eventRepository.findById(eventID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));

        // 1. Compras (obligatorio primero)
        if (event.getSessions() != null) {
            new ArrayList<>(event.getSessions()).forEach(s -> purchaseService.deletePurchasesBySession(s));
        }

        eventRepository.removeDiscountAssociations(eventID);
        eventRepository.flush();

        event.getDiscounts().clear();

        event.getEventImages().clear();
        event.getSessions().clear();
        event.getZones().clear();

        eventRepository.flush();
        eventRepository.delete(event);
        eventRepository.flush();
    }

    /**
     * Creates a new event and returns a DTO for REST API.
     *
     * @param event Event entity
     * @return EventDTO representation of the saved event
     */
    public EventDTO createEventREST(Event event) {
        return eventMapper.toDTO(save(event));
    }

    /**
     * Returns a paginated Page of events.
     *
     * @param page Page number
     * @param size Page size
     * @return Page of Event entities
     */
    public Page<Event> getEventsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAll(pageable);
    }

    /**
     * Checks if an event exists by its ID.
     *
     * @param eventID Event ID
     * @return True if the event exists
     */
    public boolean existsById(Long eventID) {
        return eventRepository.existsById(eventID);
    }

    /**
     * Returns the first page of events with a default size.
     *
     * @return Page of first events
     */
    public Page<Event> getFirstPageOfEvents() {
        return searchEvents(null, null, null, 0, 5);
    }

    public Image findEventImageById(Event event, Long imageID) {
        return event.getEventImages().stream()
                .filter(image -> image.getImageID().equals(imageID))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Imagen no encontrada para el evento con ID: " + imageID));
    }

    public Image editEventImage(Event event, MultipartFile newImage, Long imageID, boolean option)
            throws IOException, SQLException {
        byte[] bytes = imageService.loadExternalImage(newImage);
        Blob i = imageService.convertToBlob(bytes);

        Image image = !imageID.equals(0) ? findEventImageById(event, imageID) : null;
        if (option || image == null) {
            image = new Image(event.getName() + "_image", i);
            event.getEventImages().add(image);
        } else {
            image.setImageCode(i);
        }
        save(event);
        return image;
    }
}