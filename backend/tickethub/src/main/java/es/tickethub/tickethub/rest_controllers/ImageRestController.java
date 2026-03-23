package es.tickethub.tickethub.rest_controllers;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.tickethub.tickethub.dto.ImageDTO;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.mappers.ImageMapper;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.ClientService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ImageService;




@RestController
@RequestMapping("/api/v1")
public class ImageRestController {

    @Autowired private ClientService clientService;
    @Autowired private EventService eventService;
    @Autowired private ArtistService artistService;
    @Autowired private ImageService imageService;
    @Autowired private ImageMapper imageMapper;

    /* CLIENT IMAGE METHODS */
    @GetMapping("/users/{userID}/image")
    public ResponseEntity<byte[]> getClientImage(@PathVariable Long userID) {
        return imageService.getClientImageResponse(clientService.getClientById(userID));
    }

    @PostMapping("/users/{userID}/image")
    public ResponseEntity<ImageDTO> addClientImage(@PathVariable Long userID, @RequestParam MultipartFile image) throws IOException, SQLException {
        Client client = clientService.getClientById(userID);
        Image newImage = imageService.editClientImage(client, image, true);

        URI location = fromCurrentRequest().path("/{imageID}").buildAndExpand(newImage.getImageID()).toUri();
        return ResponseEntity.created(location).body(imageMapper.toDTO(newImage));
    }

    @PutMapping("/users/{userID}/image")
    public ImageDTO changeClientImage(@PathVariable Long userID, @RequestParam MultipartFile image) throws IOException, SQLException {
        Client client = clientService.getClientById(userID);
        Image newImage = imageService.editClientImage(client, image, false);
        return imageMapper.toDTO(newImage);
    }
    
    @DeleteMapping("/users/{userID}/image")
    public ImageDTO deleteClientImage(@PathVariable Long userID) {
        Client client = clientService.getClientById(userID);
        clientService.deleteClientImage(client, client.getProfileImage().getImageID());
        return imageMapper.toDTO(client.getProfileImage());
    }

    /* ARTIST IMAGE METHODS */
    @GetMapping("/public/artists/{artistID}/image")
    public ResponseEntity<byte[]> getArtistImage(@PathVariable Long artistID) {
        return imageService.getArtistImageResponse(artistService.findById(artistID));
    }

    @PostMapping("/admin/artists/{artistID}/image")
    public ResponseEntity<ImageDTO> postArtistImage(@PathVariable Long artistID, @RequestParam MultipartFile image) throws IOException, SQLException {
        Artist artist = artistService.findById(artistID);
        Image newImage = imageService.editArtistImage(artist, image, true);

        URI location = fromCurrentRequest().path("/{imageID}").buildAndExpand(newImage.getImageID()).toUri();
        return ResponseEntity.created(location).body(imageMapper.toDTO(newImage));
    }

    @PutMapping("/admin/artists/{artistID}/image")
    public ImageDTO changeArtistImage(@PathVariable Long artistID, @RequestParam MultipartFile image) throws IOException, SQLException {
        Artist artist = artistService.findById(artistID);
        imageService.editArtistImage(artist, image, false);
        return imageMapper.toDTO(artist.getArtistImage());
    }

    @DeleteMapping("/admin/artists/{artistID}/image")
    public ImageDTO deleteArtistImage(@PathVariable Long artistID) {
        Artist artist = artistService.findById(artistID);
        imageService.deleteArtistImage(artist);
        return imageMapper.toDTO(artist.getArtistImage());
    }
    
    /* EVENT IMAGES METHODS */
    @GetMapping("/public/events/{eventID}/images")
    public List<ImageDTO> getEventImages(@PathVariable Long eventID) {
        Event event = eventService.findByIdOrThrow(eventID);
        return imageMapper.toDTOs(event.getEventImages());
    }

    @GetMapping("/public/events/{eventID}/images/{imageID}")
    public ResponseEntity<byte[]> getEventImage(@PathVariable Long eventID, @PathVariable Long imageID) {
        Event event = eventService.findByIdOrThrow(eventID);
        return imageService.getEventImageResponse(event, imageID);
    }

    @PostMapping("/admin/events/{eventID}/images")
    public ResponseEntity<ImageDTO> postMethodName(@PathVariable Long eventID, @RequestParam MultipartFile image) throws IOException, SQLException {
        Event event = eventService.findByIdOrThrow(eventID);
        Image newImage = eventService.editEventImage(event, image, (long) 0, true);

        URI location = fromCurrentRequest().path("/{imageID}").buildAndExpand(newImage.getImageID()).toUri();
        return ResponseEntity.created(location).body(imageMapper.toDTO(newImage));
    }
    
    @PutMapping("/admin/events/{eventID}/images/{imageID}")
    public ImageDTO putMethodName(@PathVariable Long eventID, @PathVariable Long imageID, @RequestParam MultipartFile image) throws IOException, SQLException {
        Event event = eventService.findByIdOrThrow(imageID);
        Image newImage = eventService.editEventImage(event, image, imageID, false);
        return imageMapper.toDTO(newImage);
    }

    @DeleteMapping("/admin/events/{eventID}/images/{imageID}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long eventID, @PathVariable Long imageID) {
        eventService.deleteEventImage(eventID, imageID);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/events/{eventID}/images")
    public ResponseEntity<Void> deleteEventImages(@PathVariable Long eventID) {
        Event event = eventService.findByIdOrThrow(eventID);
        List<Long> imageIds = event.getEventImages().stream().map(image -> image.getImageID()).toList();
        while (!imageIds.isEmpty()) {
            eventService.deleteEventImage(eventID, imageIds.getFirst());
            imageIds.removeFirst();
        }
        return ResponseEntity.noContent().build();
    }

}
