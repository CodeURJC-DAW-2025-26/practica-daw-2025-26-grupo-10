package es.tickethub.tickethub.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.repositories.ArtistRepository;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService (ArtistRepository artistRepository){
        this.artistRepository = artistRepository;
    }

    public List<Artist> findAll(){
        List<Artist> artists = artistRepository.findAll();
        if (artists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay artistas");
        }
        return artists;
    }

    public Artist findById(Long id){
        Optional <Artist> optionalArtist = artistRepository.findById(id);
        if (optionalArtist.isPresent()){
            return optionalArtist.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado");
    }
    public Artist findByName (String name){
        Optional <Artist> optionalArtist = artistRepository.findByArtistName(name);
        if (optionalArtist.isPresent()){
            return optionalArtist.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado");
    }

    public Artist saveAndEditArtist(Artist artist) {
        
        return artistRepository.save(artist);
    }

    public void deleteById(Long id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (!optionalArtist.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }
        Artist artist = optionalArtist.get();
        artistRepository.deleteById(artist.getArtistID()); 
    }

    public List<Artist> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return artistRepository.findAll(pageable).getContent();
    }

    public Page<Artist> searchArtists(String name, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.isBlank()) {
            return artistRepository.findAll(pageable);
        }

        return artistRepository.findByArtistNameContainingIgnoreCase(name, pageable);
    } 

}



