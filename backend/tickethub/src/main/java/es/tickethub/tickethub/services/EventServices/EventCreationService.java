package es.tickethub.tickethub.services.EventServices;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.ImageService;


@Service
public class EventCreationService {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ImageService imageService;

    public void prepareEventForCreation(Event event, Long artistID, MultipartFile[] files) {
        Artist artist = artistService.findById(artistID);
        event.setArtist(artist);
        artist.getEventsIncoming().add(event);
        if (files != null && files.length > 0) {
            event.getEventImages().addAll(imageService.createImagesFromFiles(files));
        }
        event.setCapacity(
            event.getZones().stream().mapToInt(Zone::getCapacity).sum()
        );
    }
}