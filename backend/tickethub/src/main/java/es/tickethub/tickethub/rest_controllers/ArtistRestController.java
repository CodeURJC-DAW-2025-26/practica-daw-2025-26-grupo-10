package es.tickethub.tickethub.rest_controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import es.tickethub.tickethub.dto.ArtistBasicDTO;
import es.tickethub.tickethub.dto.ArtistCreateDTO;
import es.tickethub.tickethub.dto.ArtistDTO;
import es.tickethub.tickethub.dto.ArtistUpdateDTO;

import org.springframework.data.domain.Pageable;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.mappers.ArtistMapper;
import es.tickethub.tickethub.services.ArtistService;

@RestController
@RequestMapping("/api/v1/artists")
public class ArtistRestController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistMapper artistMapper;

    @GetMapping
    public List<ArtistBasicDTO> getAllArtists(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "artistName") String sortField) {

    List<String> allowed = List.of("artistName", "artistID");
    if (!allowed.contains(sortField)) {
        sortField = "artistName";
    }

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortField));
    Page<Artist> artistPage = artistService.findPaginated(pageable);

    return artistPage.getContent()
            .stream()
            .map(artistMapper::toBasicDTO)
            .toList();
    }

    @GetMapping("/{id}")
    public ArtistDTO getArtist(@PathVariable Long id) {
        return artistMapper.toDTO(artistService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistDTO createArtist(@RequestBody ArtistCreateDTO dto) {
        Artist artist = artistMapper.toEntity(dto);
        
        return artistMapper.toDTO(artistService.save(artist));
    }

    @PutMapping("/{id}")
    public ArtistDTO updateArtist(@PathVariable Long id, @RequestBody ArtistUpdateDTO dto) {
        Artist existing = artistService.findById(id);
        artistMapper.updateEntityFromDto(dto, existing);

        return artistMapper.toDTO(artistService.save(existing));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtist(@PathVariable Long id) {
        artistService.deleteById(id);
    }
}