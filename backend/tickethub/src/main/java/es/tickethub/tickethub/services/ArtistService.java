package es.tickethub.tickethub.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.sql.Blob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.services.ArtistService;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import es.tickethub.tickethub.repositories.ArtistRepository;
@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Artist findById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado"));
    }

    public Artist findByName(String name) {
        return artistRepository.findByArtistName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado"));
    }

    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }

    public void deleteById(Long id) {
        if (!artistRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }
        artistRepository.deleteById(id);
    }

    public Page<Artist> findPaginated(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    public Page<Artist> searchArtists(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.isBlank()) {
            return artistRepository.findAll(pageable);
        }

        return artistRepository.findByArtistNameContainingIgnoreCase(name, pageable);
    }

    public void assignImage(Artist artist, MultipartFile file) throws IOException, SQLException {
        if (file != null && !file.isEmpty()) {
            Blob blob = new SerialBlob(file.getBytes());
            Image image = new Image(file.getOriginalFilename(), blob);
            artist.setArtistImage(image);
        }
    }

    public List<Artist> getPopularArtists(int limit) {
        return findAll().stream()
                .sorted((a1, a2) -> {
                    int count1 = ((a1.getEventsIncoming() != null) ? a1.getEventsIncoming().size() : 0)
                            + ((a1.getLastEvents() != null) ? a1.getLastEvents().size() : 0);
                    int count2 = ((a2.getEventsIncoming() != null) ? a2.getEventsIncoming().size() : 0)
                            + ((a2.getLastEvents() != null) ? a2.getLastEvents().size() : 0);
                    return Integer.compare(count2, count1);
                })
                .limit(limit)
                .toList();
    }
}