package es.tickethub.tickethub.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.repositories.ZoneRepository;
/* VALIDATIONS ARE IN EVENT.JAVA & ZONE.JAVA */
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

    public Zone saveAndEditZone(Zone zone) {

        if (zone.getId() == null) {
            return zoneRepository.save(zone);
        } else {
            if (zone.getCapacity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La capacidad debe ser mayor que 0");
            }

            if (zone.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio no puede ser negativo");
            }

            Optional <Zone> existing = zoneRepository.findById(zone.getId());

            //each setter to update
            existing.get().setName(zone.getName());
            existing.get().setCapacity(zone.getCapacity());
            existing.get().setPrice(zone.getPrice());
            existing.get().setTickets(zone.getTickets());

            return zoneRepository.save(existing.get());
        }
    }

    public void deleteById(Long id) {
        Optional<Zone> optionalDiscount = zoneRepository.findById(id);
        if (!optionalDiscount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado");
        }
        Zone discount = optionalDiscount.get();
        zoneRepository.deleteById(discount.getId()); 
    }
}

