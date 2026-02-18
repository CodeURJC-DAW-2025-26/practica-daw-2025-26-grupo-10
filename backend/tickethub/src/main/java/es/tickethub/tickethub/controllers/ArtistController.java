package es.tickethub.tickethub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.services.ArtistService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public String listArtists(Model model) {
        model.addAttribute("artists", artistService.findAll());
        return "artists";
    }


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("artist", new Artist());
        return "create-artist";
    }

    @PostMapping
    public String createArtist(@Valid Artist artist, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create-artist";
        }
        try {
            artistService.save(artist);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "create-artist";
        }
        return "redirect:/artists";
    }

    @GetMapping("/{id}")
    public String showArtistDetails(@PathVariable Long id, Model model) {
        Artist artist = artistService.findById(id);
        model.addAttribute("artist", artist);
        return "artist";
    }

    @GetMapping("/delete/{id}")
    public String deleteArtist(@PathVariable Long id) {
        artistService.deleteById(id);
        return "redirect:/artists";
    }
}
