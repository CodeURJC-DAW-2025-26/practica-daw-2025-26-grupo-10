package es.tickethub.tickethub.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ui.Model;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;


public class AdminControllerHelper {
    public static void addingAttributesCreateEvent(Model model, Event event, List<Artist> allArtists, List<Zone> allZones,
            List<Discount> allDiscounts) {
        model.addAttribute("event", event);
        model.addAttribute("allArtists", allArtists);
        model.addAttribute("allZones", allZones);
        model.addAttribute("allDiscounts", allDiscounts);

        // This is to mark the correct target age when editing an event
        for (int i = 0; i <= 6; i++) {
            model.addAttribute("targetAge" + i, event.getTargetAge() == i);
        }
    }
    public static void addingZonesAndDiscounts(Model model, Event event, List<Discount> allDiscounts) {

        List<Map<String, Object>> eventDiscountsForTemplate = new ArrayList<>();

        for (Discount d : event.getDiscounts()) {
            d.setSelected(true);

            List<Map<String, Object>> allDiscountsCopy = new ArrayList<>();

            for (Discount discount : allDiscounts) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", discount.getDiscountID());
                map.put("name", discount.getDiscountName());
                map.put("selected", discount.isSelected());
                allDiscountsCopy.add(map);
            }

            Map<String, Object> row = new HashMap<>();
            row.put("value", d.getAmmount());
            row.put("isPercent", d.getPercentage().equals(true));
            row.put("isAmmount", d.getPercentage().equals(false));
            row.put("allDiscounts", allDiscountsCopy);

            eventDiscountsForTemplate.add(row);
        }

        model.addAttribute("eventDiscounts", eventDiscountsForTemplate);
    }
}
