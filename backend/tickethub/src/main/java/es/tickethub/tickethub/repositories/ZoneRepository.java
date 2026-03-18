package es.tickethub.tickethub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Zone;

//Id = Zone, type = Long

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Optional<Zone> findByIdAndEventEventID(Long zoneId, Long eventId);
}