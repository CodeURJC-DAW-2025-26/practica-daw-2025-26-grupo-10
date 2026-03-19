package es.tickethub.tickethub.services.EventServices;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.ArtistService;


@Service
public class EventRelationService {

    @Autowired
    private ArtistService artistService;

    public void updateArtist(Event event, Long newArtistID) {

        Artist oldArtist = event.getArtist();
        Artist newArtist = artistService.findById(newArtistID);

        if (!oldArtist.getArtistID().equals(newArtist.getArtistID())) {

            oldArtist.getEventsIncoming().remove(event);
            oldArtist.getLastEvents().remove(event);

            newArtist.getEventsIncoming().add(event);
            event.setArtist(newArtist);
        }
    }
}