package es.tickethub.tickethub.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
        return zoneRepository.findAll();
    }

    public Optional<Zone> findById(Long id) {
        return zoneRepository.findById(id);
    }

    public Zone save(Zone zone) {
        return zoneRepository.save(zone);
    }

    public void deleteById(Long id) {
        zoneRepository.deleteById(id);
    }
}

