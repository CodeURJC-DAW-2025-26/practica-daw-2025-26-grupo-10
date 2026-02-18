package es.tickethub.tickethub.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Zone;

//Id = Zone, type = Long


public interface ZoneRepository extends JpaRepository<Zone, Long> {
}