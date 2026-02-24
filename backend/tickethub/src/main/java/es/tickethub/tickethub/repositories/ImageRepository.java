package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
