package es.tickethub.tickethub.rest_controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.EventBasicDTO;
import es.tickethub.tickethub.dto.EventDTO;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.mappers.EventMapper;
import es.tickethub.tickethub.services.EventService;

@RestController
@RequestMapping("/api/v1/public/events")
public class PublicEventRestController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;


    @GetMapping
    public ResponseEntity<Page<EventBasicDTO>> searchEvents(
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Event> eventPage = eventService.searchEvents(artist, category, date, page, size);
        Page<EventBasicDTO> dtoPage = eventPage.map(eventMapper::toBasicDTO);
        
        return ResponseEntity.ok(dtoPage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventDetail(@PathVariable Long id) {
        Event event = eventService.findByIdOrThrow(id);
        return ResponseEntity.ok(eventMapper.toDTO(event));
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return eventService.getUniqueCategories();
    }
}