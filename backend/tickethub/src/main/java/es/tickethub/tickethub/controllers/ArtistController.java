package es.tickethub.tickethub.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.services.ArtistService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping("/public/artists")
    public String artists(Model model) {
        model.addAttribute("artists", artistService.findPaginated(0, 5));
        return "public/artists";
    }

    @GetMapping("admin/artists/create_artist")
    public String createArtist(Model model) {

        model.addAttribute("artist", new Artist());
        return "admin/artists/create_artist";
    }

    //BindingResult --> where spring keep validation errors
    @PostMapping("admin/artists/create-artist")
    public String createArtist(@Valid Artist artist, BindingResult result, Model model) {
        model.addAttribute("artist", new Artist());
        if (result.hasErrors()) {
            return "create-artist";
        }
        try {
            artistService.save(artist);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "create-artist";
        }
        return "redirect://admin/artists/manage_artists";
    }

    @GetMapping("public/artist/{id}")
    public String showArtistDetails(@PathVariable Long id, Model model) {
        Artist artist = artistService.findById(id);
        model.addAttribute("artist", artist);
        return "public/artist";
    }

    @GetMapping("/delete-artist/{id}")
    public String deleteArtist(@PathVariable Long id) {
        artistService.deleteById(id);
        return "redirect://admin/artists/manage_artists";
    }

    @GetMapping("admin/editArtist/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Artist artist = artistService.findById(id);
        model.addAttribute("artist", artist);
        return "admin/artists/create_artist";
    }

    @PutMapping("/manage")
    public String updateArtist(@Valid Artist artist, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/admin/artists/manage-artist";
        }
        artistService.save(artist);
        return "redirect:/admin/artists/manage-artist";
    }

    @GetMapping("public/artists/fragments")
    public String getMoreArtists(
        @RequestParam int page,
        @RequestParam(required = false) String search,
        Model model) {

        Page<Artist> artistPage = artistService.searchArtists(search, page, 5);

        model.addAttribute("artists", artistPage.getContent());

    return "fragments/artistsFragments";
}
}
