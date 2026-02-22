package es.tickethub.tickethub.services;

import java.util.ArrayList;
import java.util.List;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.entities.Zone;


public class ClientRecommendationService {

    private Client client;
    private List<Event> events;

    public ClientRecommendationService() {
    }



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
        if (age == null) {return 0.0;}
        return RecommendationLogic.normalize(age, 0, 120);
    }

    public double getNormalizedEventType() {
        if (events.isEmpty()) {return 0.0;}
        double sum = 0.0;
        for (Event e : events) {
            sum += RecommendationLogic.categoryToNumber(e.getCategory());
        }
        double type = sum / events.size();
        return RecommendationLogic.normalize(type, 0, 3);
    }

    public double getNormalizedPrice() {
        if (events.isEmpty()){return 0.0;}
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
        return RecommendationLogic.normalize(sum / count, 0, 500); //That max is hardcoded, we depend in that prices do not go +500$
    }


    // FINAL VECTOR IN R^3
    public double[] getClientVector() {
        return new double[] {
            getNormalizedAge(),
            getNormalizedEventType(),
            getNormalizedPrice()
        };
    }
}