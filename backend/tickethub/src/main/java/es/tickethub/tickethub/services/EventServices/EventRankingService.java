package es.tickethub.tickethub.services.EventServices;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.EventService;

@Service
public class EventRankingService {

    private final EventService eventService;
    public EventRankingService(EventService eventService) {
        this.eventService = eventService;
    }

    public List<Event> getTopSellingEvents(int limit) {
        List<Event> events = eventService.findAll().stream()
                .sorted((e1, e2) -> {
                    int sales1 = e1.getSessions() == null ? 0
                            : e1.getSessions().stream()
                                    .flatMap(s -> s.getPurchases().stream())
                                    .mapToInt(p -> p.getTickets().size()).sum();
                    int sales2 = e2.getSessions() == null ? 0
                            : e2.getSessions().stream()
                                    .flatMap(s -> s.getPurchases().stream())
                                    .mapToInt(p -> p.getTickets().size()).sum();
                    return Integer.compare(sales2, sales1);
                }).limit(limit).toList();
        //Images here
        for (Event event : events) {
            if (event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                event.setMainImage(event.getEventImages().get(0));
            }
        }
        return events;
    }

    public List<Event> getNextUpcomingEvents(int limit) {
        return eventService.findAll().stream()
                .sorted(Comparator.comparing(event -> {
                    if (event.getSessions() == null || event.getSessions().isEmpty())
                        return Instant.MAX;
                    return event.getSessions().stream()
                            .map(s -> s.getDate().toInstant())
                            .filter(date -> !date.isBefore(Instant.now()))
                            .min(Instant::compareTo).orElse(Instant.MAX);
                })).limit(limit).toList();
    }
}