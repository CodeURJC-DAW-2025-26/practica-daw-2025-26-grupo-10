package es.tickethub.tickethub.services;

import java.util.ArrayList;
import java.util.List;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.entities.Zone;

public class ClientRecommendationService {

    private final Client client;
    private final List<Event> events;

    public ClientRecommendationService(Client client) {
        this.client = client;
        this.events = extractEvents(client);
    }

    // Return the list of events extracted from the client's purchases
    private List<Event> extractEvents(Client client) {
        List<Event> evs = new ArrayList<>();
        if (client.getPurchases() != null) {
            for (Purchase p : client.getPurchases()) {
                Session s = p.getSession();
                if (s != null && s.getEvent() != null) {
                    evs.add(s.getEvent());
                }
            }
        }
        return evs;
    }

    public double getNormalizedAge() {
        Integer age = this.client.getAge();
        if (age == null) {
            return 0.0;
        }
        return RecommendationLogic.normalize(age, 0, RecommendationLogic.MAX_AGE);
    }

    public double getNormalizedEventType() {
        if (events.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Event e : events) {
            sum += RecommendationLogic.categoryToNumber(e.getCategory());
        }
        double type = sum / events.size();
        return RecommendationLogic.normalize(type, 0, RecommendationLogic.MAX_EVENT_TYPE);
    }

    public double getNormalizedPrice() {
        if (events.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        int count = 0;
        for (Event e : events) {
            if (e.getZones() != null) {
                for (Zone z : e.getZones()) {
                    if (z.getPrice() != null) {
                        sum += z.getPrice().doubleValue();
                        count++;
                    }
                }
            }
        }

        if (count == 0) {
            return 0.0;
        }
        return RecommendationLogic.normalize(sum / count, 0, RecommendationLogic.MAX_PRICE);
    }

    // FINAL VECTOR IN R^3 (of the client)
    public double[] getClientVector() {
        return new double[] {
                getNormalizedAge(),
                getNormalizedEventType(),
                getNormalizedPrice()
        };
    }
}