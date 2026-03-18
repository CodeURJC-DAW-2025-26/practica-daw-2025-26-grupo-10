package es.tickethub.tickethub.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.repositories.ZoneRepository;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    @Autowired
    private EventService eventService;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

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

    public Zone updateZone(Long eventID, Long zoneID, Zone zoneData) {
        Event event = eventService.findByIdOrThrow(eventID);
        
        Zone existing = event.getZones().stream()
                .filter(z -> z.getId().equals(zoneID))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La zona no pertenece a este evento"));

        existing.setName(zoneData.getName());
        existing.setCapacity(zoneData.getCapacity());
        existing.setPrice(zoneData.getPrice());

        if (existing.getTickets() != null) {
            for (Ticket ticket : existing.getTickets()) {
                ticket.setZone(existing);
            }
        }

        updateEventCapacity(event);
        return zoneRepository.save(existing);
    }

    public void deleteZone(Long eventID, Long zoneID) {
        Event event = eventService.findByIdOrThrow(eventID);
        boolean removed = event.getZones().removeIf(z -> z.getId().equals(zoneID));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zona no encontrada en el evento");
        }
        Zone zone = findById(zoneID);
        zone.setEvent(null);
        zoneRepository.delete(zone);
        updateEventCapacity(event);
    }

    private void updateEventCapacity(Event event) {
        int totalCapacity = event.getZones().stream()
                .mapToInt(Zone::getCapacity)
                .sum();
        event.setCapacity(totalCapacity);
        eventService.save(event);
    }
}