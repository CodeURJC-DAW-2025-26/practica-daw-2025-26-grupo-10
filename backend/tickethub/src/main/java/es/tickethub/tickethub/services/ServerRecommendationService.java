package es.tickethub.tickethub.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;

@Service
public class ServerRecommendationService {

    private final EventService eventService;
    private List<Event> events = new ArrayList<>();
    private final List<double[]> eventVectors = new ArrayList<>();

    public ServerRecommendationService(EventService eventService) {
        this.eventService = eventService;
    }

    private void calculateEventVectors() {
        eventVectors.clear();
        for (Event e : events) {
            double age = normalizeTargetAge(e);
            double type = RecommendationLogic.categoryToNumber(e.getCategory());
            double price = normalizePrice(e);
            eventVectors.add(new double[] { age, type, price });
        }
    }

    private double normalizeTargetAge(Event e) {
        Integer target = e.getTargetAge();
        if (target == null)
            return 0.0;
        return RecommendationLogic.normalize(target, 0, RecommendationLogic.MAX_AGE);
    }

    private double normalizePrice(Event e) {
        if (e.getZones() == null || e.getZones().isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        int count = 0;
        for (Zone z : e.getZones()) {
            if (z.getPrice() != null) {
                sum += z.getPrice().doubleValue();
                count++;
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return RecommendationLogic.normalize(sum / count, 0, RecommendationLogic.MAX_PRICE);
    }

    /* THIS WILL BE USED IN RECOMMENDATIONSERVICE */
    public List<Event> getEvents() {
        if (events.isEmpty()) {
            events = eventService.findAll();
            calculateEventVectors();
        }

        return events;
    }

    public List<double[]> getEventVectors() {
        if (eventVectors.isEmpty() && !events.isEmpty()) {
            calculateEventVectors();
        }
        return eventVectors;
    }
}