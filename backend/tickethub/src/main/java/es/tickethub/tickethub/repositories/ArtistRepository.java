package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Artist;
import java.util.Optional;

//Id = Artist, type = Long

public interface ArtistRepository extends JpaRepository<Artist, Long>{
    Optional<Artist> findByArtistName(String name);

}