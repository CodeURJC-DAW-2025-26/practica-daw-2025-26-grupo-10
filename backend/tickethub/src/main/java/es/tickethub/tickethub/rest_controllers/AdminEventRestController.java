package es.tickethub.tickethub.rest_controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public Page <EventDTO> getEvents(Pageable pageable) {
        return eventService.getEventRepository().findAll(pageable).map(event -> eventMapper.toDTO(event));
    }
    

    @PostMapping("/")
    public ResponseEntity <Event> createEvent(@RequestBody Event event) {
        eventService.save(event);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(event.getEventID()).toUri();

        return ResponseEntity.created(location).body(event);
    }

    @PutMapping("/{eventID}")
    public ResponseEntity <Event> updateEvent(@PathVariable Long eventID, @RequestBody Event updatedEvent) {
        if (eventService.getEventRepository().existsById(eventID)) {
            updatedEvent.setEventID(eventID);
            eventService.save(updatedEvent);

            return ResponseEntity.ok(updatedEvent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{eventID}")
    public ResponseEntity <Event> deleteEvent(@PathVariable Long eventID) {
        Optional <Event> event = eventService.findById(eventID);
        if (event.isPresent()) {
            eventService.deleteById(eventID);
            return ResponseEntity.ok(event.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}