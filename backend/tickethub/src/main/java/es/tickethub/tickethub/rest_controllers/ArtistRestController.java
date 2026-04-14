package es.tickethub.tickethub.rest_controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.ArtistBasicDTO;
import es.tickethub.tickethub.dto.ArtistDTO;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.mappers.ArtistMapper;
import es.tickethub.tickethub.services.ArtistService;

@RestController
@RequestMapping("/api/v1")
public class ArtistRestController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistMapper artistMapper;

    @GetMapping("/public/artists")
    public List<ArtistBasicDTO> getPageArtists(
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

    @GetMapping("/public/artists/{artistID}")
    public ArtistDTO getArtist(@PathVariable Long artistID) {
        return artistMapper.toDTO(artistService.findById(artistID));
    }


}