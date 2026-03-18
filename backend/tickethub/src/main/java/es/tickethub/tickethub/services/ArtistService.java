package es.tickethub.tickethub.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> findAll() {
        List<Artist> artists = artistRepository.findAll();
        if (artists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay artistas");
        }
        return artists;
    }

    public Artist findById(Long id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (optionalArtist.isPresent()) {
            return optionalArtist.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado");
    }

    public Artist findByName(String name) {
        Optional<Artist> optionalArtist = artistRepository.findByArtistName(name);
        if (optionalArtist.isPresent()) {
            return optionalArtist.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado");
    }

    public Artist saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    public void deleteById(Long id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (!optionalArtist.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }
        Artist artist = optionalArtist.get();
        artistRepository.deleteById(artist.getArtistID());
    }

    public Page<Artist> findPaginated(Pageable pageable) {
        Page<Artist> artists = artistRepository.findAll(pageable);
        if (artists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay artistas");
        }
        return artists;
    }

    public Page<Artist> searchArtists(String name, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.isBlank()) {
            return artistRepository.findAll(pageable);
        }

        return artistRepository.findByArtistNameContainingIgnoreCase(name, pageable);
    }

    public Artist createArtist(Artist artist, MultipartFile file) throws IOException, SQLException {
        if (!file.isEmpty()) {
            Blob blob = new SerialBlob(file.getBytes());
            Image image = new Image(file.getOriginalFilename(), blob);
            artist.setArtistImage(image);
        }
        return artistRepository.save(artist);
    }

    public Artist updateArtist(Artist artist, MultipartFile file) throws IOException, SQLException {

        Artist existing = findById(artist.getArtistID());

        existing.setArtistName(artist.getArtistName());
        existing.setInfo(artist.getInfo());

        existing.getEventsIncoming().clear();
        existing.getEventsIncoming().addAll(artist.getEventsIncoming());

        existing.getLastEvents().clear();
        existing.getLastEvents().addAll(artist.getLastEvents());

        if (!file.isEmpty()) {
            Blob blob = new SerialBlob(file.getBytes());
            Image image = new Image(file.getOriginalFilename(), blob);
            existing.setArtistImage(image);
        }

        existing.setInstagram(artist.getInstagram());
        existing.setTwitter(artist.getTwitter());

        return artistRepository.save(existing);
    }
    //Most popular artists
    public List<Artist> getPopularArtists(int limit) {
        List<Artist> allArtists = findAll();
        return allArtists.stream()
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
