package es.tickethub.tickethub.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;

@Service
public class AdminViewService {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private DiscountService discountService;

    public void prepareEditEventView(Model model, Event event) {

        List<Artist> allArtists = artistService.findAll();
        List<Zone> allZones = event.getZones();
        List<Discount> allDiscounts = discountService.getAllDiscounts();

        markSelectedArtist(allArtists, event);
        addBaseAttributes(model, event, allArtists, allZones);
        addDiscounts(model, event, allDiscounts);
    }

    private void markSelectedArtist(List<Artist> artists, Event event) {
        for (Artist artist : artists) {
            boolean selected = event.getArtist() != null &&
                    artist.getArtistID().equals(event.getArtist().getArtistID());

            artist.setSelected(selected);
        }
    }

    private void addBaseAttributes(Model model, Event event,
            List<Artist> artists, List<Zone> zones) {

        model.addAttribute("event", event);
        model.addAttribute("allArtists", artists);
        model.addAttribute("allZones", zones);

        for (int i = 0; i <= 6; i++) {
            model.addAttribute("targetAge" + i, event.getTargetAge() == i);
        }
    }

    private void addDiscounts(Model model, Event event, List<Discount> allDiscounts) {

        List<Map<String, Object>> discountRows = new ArrayList<>();

        for (Discount d : event.getDiscounts()) {
            d.setSelected(true);

            List<Map<String, Object>> options = new ArrayList<>();

            for (Discount discount : allDiscounts) {
                Map<String, Object> option = new HashMap<>();
                option.put("discountID", discount.getDiscountID());
                option.put("discountName", discount.getDiscountName());
                option.put("amount", discount.getAmount());
                option.put("isPercentage", discount.getPercentage());
                option.put("selected", discount.getDiscountID().equals(d.getDiscountID()));

                options.add(option);
            }

            Map<String, Object> row = new HashMap<>();
            row.put("eventDiscounts", options);

            discountRows.add(row);
        }

        model.addAttribute("discountRows", discountRows);
        model.addAttribute("allDiscounts", allDiscounts);
    }
}
