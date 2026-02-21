package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.services.ArtistService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("public")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping("/artists")
    public String listArtists(Model model) {
        model.addAttribute("artists", artistService.findAll());
        return "public/artists";
    }


    @GetMapping("/artist/new")
    public String showCreateForm(Model model) {
        model.addAttribute("artist", new Artist());
        return "create-artist";
    }
    //BindingResult --> where spring keep validation errors
    @PostMapping("/create-artist")
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

    @GetMapping("artist/{id}")
    public String showArtistDetails(@PathVariable Long id, Model model) {
        Artist artist = artistService.findById(id);
        model.addAttribute("artist", artist);
        return "public/artist";
    }

    @GetMapping("/delete-artist/{id}")
    public String deleteArtist(@PathVariable Long id) {
        artistService.deleteById(id);
        return "redirect:/artists";
    }

    @GetMapping("/editArtist/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Artist artist = artistService.findById(id);
        model.addAttribute("artist", artist);
        return "manage-artist";
    }

    @PutMapping("/manage")
    public String updateArtist(@Valid Artist artist, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "manage-artist";
        }
        artistService.save(artist);
        return "redirect:/artists";
    }

}
