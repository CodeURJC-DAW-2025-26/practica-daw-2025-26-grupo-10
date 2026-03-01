package es.tickethub.tickethub.repositories;

import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.tickethub.tickethub.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByName(String name);

    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Event> findByCategoryContainingIgnoreCase(String category, Pageable pageable);

    Page<Event> findByArtist_ArtistNameContainingIgnoreCase(String artistName, Pageable pageable);

    /**
     * Finds events by artist name, category, and date range.
     * The use of (:param IS NULL OR ...) allows the query to ignore filters that
     * were not provided.
     * LEFT JOIN ensures events without sessions are still returned when no date
     * filter is applied.
     */
    @Query("SELECT DISTINCT e FROM Event e " +
            "LEFT JOIN e.sessions s WHERE " +
            "(:artist IS NULL OR LOWER(e.artist.artistName) LIKE LOWER(CONCAT('%', :artist, '%'))) AND " +
            "(:category IS NULL OR LOWER(e.category) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
            "(:startDate IS NULL OR s.date >= :startDate) AND " +
            "(:endDate IS NULL OR s.date <= :endDate)")
    Page<Event> findByFilters(
            @Param("artist") String artist,
            @Param("category") String category,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            Pageable pageable);

    @Query("SELECT DISTINCT e.category FROM Event e WHERE e.category IS NOT NULL")
    List<String> findAllUniqueCategories();        
}