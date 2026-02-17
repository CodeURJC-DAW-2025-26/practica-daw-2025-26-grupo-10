package es.tickethub.tickethub.services;

import java.util.List;
import java.util.Optional;

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
        List <Zone> zone = zoneRepository.findAll();
        if (!(zone.isEmpty())){
            return zone;
        }
        return null;
    }

    public Zone findById(Long id) {
        Optional <Zone> zoneOptional = zoneRepository.findById(id);
        if (zoneOptional.isPresent()){
            return zoneOptional.get();
        }
        return null;
    }

    public Zone save(Zone zone) {
        return zoneRepository.save(zone);

    }

    public void deleteById(Long id) {
        zoneRepository.deleteById(id);
    }
}
