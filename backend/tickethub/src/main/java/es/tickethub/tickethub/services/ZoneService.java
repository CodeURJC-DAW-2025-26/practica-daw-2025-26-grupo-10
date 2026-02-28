package es.tickethub.tickethub.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.repositories.ZoneRepository;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<Zone> findAll() {
        List <Zone> zone = zoneRepository.findAll();
        if (!(zone.isEmpty())){
            return zone;
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay zonas registradas");
    }

    public Zone findById(Long id) {
        Optional <Zone> zoneOptional = zoneRepository.findById(id);
        if (zoneOptional.isPresent()){
            return zoneOptional.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zona no encontrada");
    }

    public Zone save(Zone zone) {

        if (zone.getId() == null) {
            return zoneRepository.save(zone);
        } else {
            if (zone.getCapacity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La capacidad debe ser mayor que 0");
            }

            if (zone.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio no puede ser negativo");
            }

            return zoneRepository.save(zone);
        }
    }

    public void deleteById(Long id) {
        Optional<Zone> optionalZone = zoneRepository.findById(id);
        if (!optionalZone.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zone not found");
        }
        Zone zone = optionalZone.get();
        zone.setEvent(null);
        zoneRepository.deleteById(zone.getId());
    }
}

