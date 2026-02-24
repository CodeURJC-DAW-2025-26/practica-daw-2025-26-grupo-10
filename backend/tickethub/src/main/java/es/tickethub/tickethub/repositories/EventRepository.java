package es.tickethub.tickethub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EventRepository extends JpaRepository <Event,Long>{
    Optional <Event> findByName(String name);
    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Event> findByCategoryContainingIgnoreCase(String category, Pageable pageable);

    Page<Event> findByArtist_ArtistNameContainingIgnoreCase(String artistName, Pageable pageable);
}
