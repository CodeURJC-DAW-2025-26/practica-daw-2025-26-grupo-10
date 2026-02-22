package es.tickethub.tickethub.services;

import java.util.ArrayList;
import java.util.List;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;

public class ServerRecommendationService {

    private final List<Event> events;
    private final List<double[]> eventVectors; // This will be the vectors in R^3 for later iteration

    public ServerRecommendationService(List<Event> events) {
        if (events != null) {
            this.events = events;
        } else {
            this.events = new ArrayList<>();
        }
        this.eventVectors = new ArrayList<>();
        calculateEventVectors();
    }

    private void calculateEventVectors() {
        for (Event e : events) {
            double age = normalizeTargetAge(e);
            double type = RecommendationLogic.categoryToNumber(e.getCategory());
            double price = normalizePrice(e);
            eventVectors.add(new double[] { age, type, price });
        }
    }

    private double normalizeTargetAge(Event e) {
        Integer target = e.getTargetAge();
        if (target == null) return 0.0;
        return RecommendationLogic.normalize(target, 0, 120);
    }

    private double normalizePrice(Event e) {
        if (e.getZones() == null || e.getZones().isEmpty()){
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
        return RecommendationLogic.normalize(sum / count, 0, 500); // max hardcoded as in Client
    }

/* THIS WILL BE USED IN RECOMMENDATIONSERVICE */
    public List<Event> getEvents() {
        return events;
    }

    public List<double[]> getEventVectors() {
        return eventVectors;
    }
}