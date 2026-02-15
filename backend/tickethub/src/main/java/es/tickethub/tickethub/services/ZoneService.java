package es.tickethub.tickethub.services;

import java.util.List;
import org.springframework.stereotype.Service;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.repositories.ZoneRepository;

@Service
public class ZoneService {
//Aun no sé si estos son todos los métodos necesarios
    private final ZoneRepository zoneRepository;

    //Spring inyecta automáticamente el repository de SQL
    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<Zone> findAll() {
        return zoneRepository.findAll();
    }

    public Zone findById(Long id) {
        return zoneRepository.findById(id).orElse(null);
    }

    public Zone save(Zone zone) {
        return zoneRepository.save(zone);
    }

    public void deleteById(Long id) {
        zoneRepository.deleteById(id);
    }
}
