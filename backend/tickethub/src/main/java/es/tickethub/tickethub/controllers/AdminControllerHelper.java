package es.tickethub.tickethub.controllers;

import java.util.List;
import org.springframework.ui.Model;
import es.tickethub.tickethub.entities.Artist;
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

}
