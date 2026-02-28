package es.tickethub.tickethub.controllers;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.services.ArtistService;
import jakarta.validation.Valid;


@Controller
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    /*------------------------------------- FUNCTIONS FOR THE PUBLIC FOLDER ---------------------------------*/
    @GetMapping("/public/artists")
    public String artists(Model model) {
        model.addAttribute("artists", artistService.findPaginated(0, 5));
        return "public/artists";
    }

    @GetMapping("/public/artist/{id}")
    public String showArtistDetails(@PathVariable Long id, Model model) {
        Artist artist = artistService.findById(id);
        model.addAttribute("artist", artist);
        return "public/artist";
    }

    @GetMapping("/public/artists/fragments")
    public String getMoreArtists(
        @RequestParam int page,
        @RequestParam(required = false) String search,
        Model model) {

        Page<Artist> artistPage = artistService.searchArtists(search, page, 5);

        model.addAttribute("artists", artistPage.getContent());

        return "fragments/artistsFragments";
    }

    /*------------------------------------- FUNCTIONS FOR THE ADMIN FOLDER ---------------------------------*/

    /* Charge the admin view artists*/
    @GetMapping("/admin/artists/manage_artists")
    public String showAdminViewArtists(Model model) {

        model.addAttribute("artists", artistService.findAll());
        return "admin/artists/manage_artists";
    }

    /* Show the edit_artist page*/
    @GetMapping("/admin/artists/edit_artist/{artistID}")
    public String showEditArtist(@PathVariable Long artistID, Model model) {
        Artist artist = artistService.findById(artistID);

        model.addAttribute("artist", artist);
        return "admin/artists/create_artist";
    }

    @GetMapping("/admin/artists/create_artist")
    public String showCreateArtist(Model model) {
        return "admin/artists/create_artist";
    }

    //BindingResult --> where spring keep validation errors
    @PostMapping("/admin/artists/create_artist")
    public String createArtist(@Valid Artist artist, BindingResult result, @RequestParam("image") MultipartFile file, Model model) {
        
        if (result.hasErrors()) {
            return "create_artist";
        }
        try {
            if (!file.isEmpty()) {
                Blob blob = new SerialBlob(file.getBytes());
                Image image = new Image(file.getOriginalFilename(), blob);
                artist.setArtistImage(image);
            }

            artistService.saveArtist(artist);
        } catch (IOException | SQLException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/artists/create_artist";
        }
        return "redirect:/admin/artists/manage_artists";
    }

    @PostMapping("/admin/artists/edit_artist/{artistID}")
    public String editArtist(@Valid Artist artist, BindingResult result, @RequestParam("image") MultipartFile file, Model model) {
        
        if (result.hasErrors()) {
            return "/admin/artists/create_artist";
        }

        try {
            Artist existing = artistService.findById(artist.getArtistID());

            //each setter to update the info
            existing.setArtistName(artist.getArtistName());
            existing.setInfo(artist.getInfo());

            existing.getEventsIncoming().clear();
            existing.getEventsIncoming().addAll(artist.getEventsIncoming());

            existing.getLastEvents().clear();
            existing.getLastEvents().addAll(artist.getLastEvents());
            if (!file.isEmpty()) {
                Blob blob = new SerialBlob(file.getBytes());
                Image image = new Image(file.getOriginalFilename(), blob);
                artist.setArtistImage(image);
                existing.setArtistImage(artist.getArtistImage());
            }
            existing.setInstagram(artist.getInstagram());
            existing.setTwitter(artist.getTwitter());
            artistService.saveArtist(artist);
            
        } catch (IOException | SQLException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/artists/create_artist";
        }
        return "redirect:/admin/artists/manage_artists";
    }

    @DeleteMapping("/admin/artists/delete_artist/{artistID}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteArtist(@PathVariable Long artistID) {
        artistService.deleteById(artistID);
    }

}
