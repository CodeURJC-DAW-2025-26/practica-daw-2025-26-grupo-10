package es.tickethub.tickethub.services.EventServices;

import java.util.List;

import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.ClientRecommendationService;
import es.tickethub.tickethub.services.RecommendationService;
import es.tickethub.tickethub.services.ServerRecommendationService;

@Service
public class EventRecommendationService {

    private final RecommendationService recommendationService;
    private final ServerRecommendationService serverService;

    public EventRecommendationService(
            RecommendationService recommendationService,
            ServerRecommendationService serverService) {
        this.recommendationService = recommendationService;
        this.serverService = serverService;
    }

    public List<Event> getRecommendedEvents(Client client, int limit) {
        ClientRecommendationService crs = new ClientRecommendationService(client);
        return recommendationService.recommendEvents(crs, serverService, limit, true);
    }
}