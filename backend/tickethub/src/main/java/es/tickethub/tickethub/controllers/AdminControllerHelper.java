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
    public static void addingAttributesCreateEvent(Model model, Event event, List<Artist> allArtists, List<Zone> allZones) {

        model.addAttribute("event", event);
        model.addAttribute("allArtists", allArtists);
        model.addAttribute("allZones", allZones);
        // This is to mark the correct target age when editing an event
        for (int i = 0; i <= 6; i++) {
            model.addAttribute("targetAge" + i, event.getTargetAge() == i);
        }
    }

    public static void addingDiscounts(Model model, Event event, List<Discount> allDiscounts) {

        List<Map<String, Object>> discountRows = new ArrayList<>();

        for (Discount d : event.getDiscounts()) {
            d.setSelected(true);

            List<Map<String, Object>> options = new ArrayList<>();

            for (Discount discount : allDiscounts) {
                Map<String, Object> option = new HashMap<>();
                option.put("discountID", discount.getDiscountID());
                option.put("discountName", discount.getDiscountName());
                option.put("ammount", discount.getAmmount());
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
