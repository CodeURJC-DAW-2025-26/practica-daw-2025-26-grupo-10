package es.tickethub.tickethub.rest_controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.tickethub.tickethub.dto.EventCreateDTO;
import es.tickethub.tickethub.dto.EventDTO;
import es.tickethub.tickethub.dto.EventUpdateDTO;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.mappers.EventMapper;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.EventServices.EventCreationService;
import es.tickethub.tickethub.services.EventServices.EventRelationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/events")
public class AdminEventRestController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventCreationService eventCreationService;

    @Autowired
    private EventRelationService eventRelationService;

    @Autowired
    private EventMapper eventMapper;

    @GetMapping
    public Page<EventDTO> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return eventService.getEventsPage(page, size).map(eventMapper::toDTO);
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventCreateDTO createEventDTO) {
        Event event = eventMapper.toEntity(createEventDTO);
        eventCreationService.prepareEventForCreation(event, createEventDTO.artistId(), null);
        eventService.save(event);
        URI location = fromCurrentRequest().path("/{eventID}").buildAndExpand(event.getEventID()).toUri();
        return ResponseEntity.created(location).body(eventMapper.toDTO(event));
    }

    @PutMapping("/{eventID}")
    public EventDTO updateEvent(@PathVariable Long eventID, @Valid @RequestBody EventUpdateDTO updatedEventDTO) {
        Event existingEvent = eventService.findByIdOrThrow(eventID);
        eventRelationService.updateArtist(existingEvent, updatedEventDTO.artistId());
        eventMapper.updateEntityFromDto(updatedEventDTO, existingEvent);
        eventService.save(existingEvent);

        return eventMapper.toDTO(existingEvent);
    }

    @DeleteMapping("/{eventID}")
    public EventDTO deleteEvent(@PathVariable Long eventID) {
        Event event = eventService.findByIdOrThrow(eventID);
        eventService.deleteEvent(eventID);
        return eventMapper.toDTO(event);
    }

}