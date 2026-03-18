package es.tickethub.tickethub.rest_controllers;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;

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

import es.tickethub.tickethub.dto.EventDTO;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.mappers.EventMapper;
import es.tickethub.tickethub.services.EventService;

@RestController
@RequestMapping("/api/v1/admin/events")
public class AdminEventRestController {
    
    @Autowired
    private EventService eventService;



    @Autowired
    private EventMapper eventMapper;

    @GetMapping("/")
    public Page<EventDTO> getEvents(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        return eventService.getEventsPage(page, size)
                        .map(eventMapper::toDTO);
    }
    

    @PostMapping("/")
    public ResponseEntity <EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);
        eventService.save(event);

        URI location = fromCurrentRequest().path("/{eventID}").buildAndExpand(event.getEventID()).toUri();

        return ResponseEntity.created(location).body(eventMapper.toDTO(event));
    }

    @PutMapping("/{eventID}")
    public EventDTO updateEvent(@PathVariable Long eventID, @RequestBody EventDTO updatedEventDTO) {
        if (eventService.existsById(eventID)) {
            
            Event updatedEvent = eventMapper.toEntity(updatedEventDTO);
            
            updatedEvent.setEventID(eventID);
            eventService.save(updatedEvent);

            return eventMapper.toDTO(updatedEvent);
        } else {
            throw new NoSuchElementException();
        }
    }

    @DeleteMapping("/{eventID}")
    public EventDTO deleteEvent(@PathVariable Long eventID) {
        Optional <Event> event = eventService.findById(eventID);
        if (event.isPresent()) {
            eventService.deleteById(eventID);
        }
        return eventMapper.toDTO(event.get());
    }
}