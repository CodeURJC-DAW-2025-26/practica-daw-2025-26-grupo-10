package es.tickethub.tickethub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.repositories.EventRepository;
import es.tickethub.tickethub.repositories.ZoneRepository;

@Service
public class ZoneService {

    @Autowired
    private EventService eventService;

    @Autowired
    ZoneRepository zoneRepository;
    @Autowired
    EventRepository eventRepository;

    public List<Zone> findAll() {
        return zoneRepository.findAll();
    }

    public Zone findById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zona no encontrada"));
    }

    public Zone createZone(Long eventID, Zone zoneData) {
        Event event = eventService.findByIdOrThrow(eventID);
        
        Zone newZone = new Zone(zoneData.getName(), zoneData.getCapacity(), zoneData.getPrice());
        newZone.setEvent(event);
        event.getZones().add(newZone);

        updateEventCapacity(event);
        return zoneRepository.save(newZone);
    }

    @Transactional
    public Zone createAndAssignEvent(Zone zone, Long eventID) {
        Event event = eventRepository.findById(eventID).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El evento con ID " + eventID + " no existe"));

        Zone newZone = new Zone(zone.getName(), zone.getCapacity(), zone.getPrice());
        newZone.setEvent(event);
        event.getZones().add(newZone);

        int totalCapacity = event.getZones().stream().mapToInt(Zone::getCapacity).sum();
        event.setCapacity(totalCapacity);

        eventRepository.save(event);

        return zoneRepository.save(newZone);
    }

    public Zone findByEventAndID(Long eventID, Long zoneID) {
        return zoneRepository.findByIdAndEventEventID(zoneID, eventID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "La zona " + zoneID + " no pertenece al evento " + eventID));
    }

    @Transactional
    public Zone updateZone(Long eventId, Long zoneId, Zone zone) {
        Zone existing = findByEventAndID(eventId, zoneId);
        Event event = existing.getEvent();

        existing.setName(zone.getName());
        existing.setCapacity(zone.getCapacity());
        existing.setPrice(zone.getPrice());

        if (existing.getTickets() != null) {
            existing.getTickets().forEach(ticket -> ticket.setZone(existing));
        }

        int newTotalCapacity = event.getZones().stream().mapToInt(Zone::getCapacity).sum();
        event.setCapacity(newTotalCapacity);

        return zoneRepository.save(existing);
    }

    @Transactional
    public void deleteZoneFromEvent (Long eventID, Long zoneID) {
        Zone zone = zoneRepository.findByIdAndEventEventID(zoneID, eventID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La zona " + zoneID + " no pertenece al evento " + eventID));;
        Event event = zone.getEvent();

        event.getZones().remove(zone);

        int updatedCapacity = event.getZones().stream().mapToInt(Zone::getCapacity).sum();
        event.setCapacity(updatedCapacity);

        eventRepository.save(event);
        zoneRepository.delete(zone);
    }

    private void updateEventCapacity(Event event) {
        int totalCapacity = event.getZones().stream()
                .mapToInt(Zone::getCapacity)
                .sum();
        event.setCapacity(totalCapacity);
        eventService.save(event);
    }
}