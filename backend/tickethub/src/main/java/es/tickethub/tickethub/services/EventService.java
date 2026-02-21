package es.tickethub.tickethub.services;

import java.util.List;
import java.util.Optional;

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
}
