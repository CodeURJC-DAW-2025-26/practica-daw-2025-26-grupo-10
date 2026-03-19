package es.tickethub.tickethub.services;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;


@Service
public class EventManagementService {

    @Autowired
    private EventService eventService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ImageService imageService;

    public Event create(Event event, Long artistID, MultipartFile[] files) {

        Artist artist = artistService.findById(artistID);
        event.setArtist(artist);
        artist.getEventsIncoming().add(event);
        if (files != null && files.length > 0) {
            event.getEventImages().addAll(imageService.createImagesFromFiles(files));
        }

        event.setCapacity(calculateTotalCapacity(event.getZones()));
        return eventService.save(event);
    }

    public int calculateTotalCapacity(List<Zone> zones) {
        return zones.stream().mapToInt(Zone::getCapacity).sum();
    }
}