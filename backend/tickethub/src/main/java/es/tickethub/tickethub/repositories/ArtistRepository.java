package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    
}
