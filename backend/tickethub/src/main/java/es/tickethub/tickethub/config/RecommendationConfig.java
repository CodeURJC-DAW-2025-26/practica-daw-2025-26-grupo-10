package es.tickethub.tickethub.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ServerRecommendationService;

@Configuration
public class RecommendationConfig {

    private final EventService eventService;

    public RecommendationConfig(EventService eventService) {
        this.eventService = eventService;
    }

    @Bean
    public ServerRecommendationService serverRecommendationService() {
        List<Event> events;
        try {
            events = eventService.findAll();
        } catch (ResponseStatusException e) {
            events = new ArrayList<>();
        }
        return new ServerRecommendationService(events);
    }
}