package es.tickethub.tickethub.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.repositories.EventRepository;

@Service
public class EventService {
    private final EventRepository eventRepository;
    
    public EventService (EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll(){
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay eventos");
        }
        return events;
    }

    public Event findById(Long id){
        Optional <Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()){
            return optionalEvent.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado");
    }

    public Event findByName(String name){
        Optional <Event> optionalEvent = eventRepository.findByName(name);
        if (optionalEvent.isPresent()){
            return optionalEvent.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado");
    }

    public Event save(Event event){
        //I do not know if we need validation here
        return eventRepository.save(event);
    }
    
    public void deleteById(Long id){
        if (!(eventRepository.existsById(id))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado");
        }
        eventRepository.deleteById(id);
    }

    public List<Event> findPaginated(int page, int size) {
        // request for "give page X, size Y"
        Pageable pageable = PageRequest.of(page, size);
        // repo returns a Page object -> we use getContent to extract the list of events
        return eventRepository.findAll(pageable).getContent();
    }
    public Page<Event> searchEvents(String artist, String category, int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

        if ((artist == null || artist.isBlank()) &&
            (category == null || category.isBlank())) {
            return eventRepository.findAll(pageable);
        }

        if (artist != null && !artist.isBlank()) {
            return eventRepository
                    .findByArtist_ArtistNameContainingIgnoreCase(artist, pageable);
        }

        return eventRepository
                .findByCategoryContainingIgnoreCase(category, pageable);
    }
}
