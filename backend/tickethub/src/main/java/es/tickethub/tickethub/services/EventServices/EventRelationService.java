package es.tickethub.tickethub.services.EventServices;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.DiscountService;


@Service
public class EventRelationService {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private DiscountService discountService;

    public void syncDiscounts(Event event, List<Long> discountIDs) {
        for (Discount discount : new ArrayList<>(event.getDiscounts())) {
            discount.getEvents().remove(event);
        }
        event.getDiscounts().clear();
        if (discountIDs == null) return;
        for (Long discountID : discountIDs) {
            Discount discount = discountService.findById(discountID);
            if (!discount.getEvents().contains(event)) {
                discount.getEvents().add(event);
            }
            event.getDiscounts().add(discount);
        }
    }

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

    public void addZones(Event event, List<Zone> zones) {
    if (zones == null) return;
    for (Zone zone : zones) {
        zone.setEvent(event);
        event.getZones().add(zone);
    }
}
}