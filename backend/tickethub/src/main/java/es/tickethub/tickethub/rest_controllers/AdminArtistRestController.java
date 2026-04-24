package es.tickethub.tickethub.rest_controllers;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import es.tickethub.tickethub.dto.ArtistCreateUpdateDTO;
import es.tickethub.tickethub.dto.ArtistDTO;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.mappers.ArtistMapper;
import es.tickethub.tickethub.services.ArtistService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/artists")
public class AdminArtistRestController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistMapper artistMapper;

    @GetMapping
    public Page<ArtistDTO> getArtists(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return artistService.getArtistsPage(page, size).map(artistMapper::toDTO);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArtistDTO> createArtist(
            @Valid @RequestPart("data") ArtistCreateUpdateDTO createArtistDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException, SQLException {

        Artist artist = artistMapper.toEntity(createArtistDTO);
        if (image != null)
            artistService.assignImage(artist, image);
        artistService.save(artist);

        URI location = fromCurrentRequest().path("/{artistID}").buildAndExpand(artist.getArtistID()).toUri();
        return ResponseEntity.created(location).body(artistMapper.toDTO(artist));
    }

    @PutMapping(value = "/{artistID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArtistDTO updateArtist(
            @PathVariable Long artistID,
            @Valid @RequestPart("data") ArtistCreateUpdateDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException, SQLException {

        Artist existing = artistService.findById(artistID);
        artistMapper.updateEntityFromDto(dto, existing);
        if (image != null)
            artistService.assignImage(existing, image);

        return artistMapper.toDTO(artistService.save(existing));
    }

    @DeleteMapping("/{artistID}")
    public ArtistDTO deleteArtist(@PathVariable Long artistID) {
        Artist artist = artistService.findById(artistID);
        artistService.deleteById(artistID);
        return artistMapper.toDTO(artist);
    }
}
