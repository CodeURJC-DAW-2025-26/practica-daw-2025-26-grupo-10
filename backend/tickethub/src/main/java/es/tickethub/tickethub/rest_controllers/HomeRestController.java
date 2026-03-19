package es.tickethub.tickethub.rest_controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.ArtistBasicDTO;
import es.tickethub.tickethub.dto.EventBasicDTO;
import es.tickethub.tickethub.dto.IndexResponseDTO;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.mappers.ArtistMapper;
import es.tickethub.tickethub.mappers.EventMapper;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.ClientService;
import es.tickethub.tickethub.services.EventServices.EventRankingService;
import es.tickethub.tickethub.services.EventServices.EventRecommendationService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1/public")
public class HomeRestController {
    
    private final EventRankingService eventRankingService;
    private final EventRecommendationService eventRecommendationService;
    private final ClientService clientService;
    private final ArtistService artistService;
    private final EventMapper eventMapper;
    private final ArtistMapper artistMapper;

    public HomeRestController(EventRankingService eventRankingService,EventRecommendationService eventRecommendationService, 
        ClientService clientService, ArtistService artistService, EventMapper eventMapper, ArtistMapper artistMapper){
            this.eventRankingService=eventRankingService;
            this.eventRecommendationService = eventRecommendationService;
            this.clientService = clientService;
            this.artistService = artistService;
            this.eventMapper = eventMapper;
            this.artistMapper = artistMapper;
    }


    @GetMapping("/index")
    public ResponseEntity<IndexResponseDTO> showIndex(@AuthenticationPrincipal UserDetails userDetails) {
        List<Event> eventsTop = eventRankingService.getTopSellingEvents(3);
        List<Event> eventsBottom = eventRankingService.getNextUpcomingEvents(2);
        List<Artist> artists = artistService.getPopularArtists(4);

        List<EventBasicDTO> topDTOs = eventsTop.stream()
                .map(event -> eventMapper.toBasicDTO(event))
                .toList();
        List<EventBasicDTO> bottomDTOs = eventsBottom.stream()
                .map(event -> eventMapper.toBasicDTO(event))
                .toList();

        List<ArtistBasicDTO> artistsDTOs = artists.stream()
                .map(artist -> artistMapper.toBasicDTO(artist))
                .toList();

        List<EventBasicDTO> recommendedDTOs = new ArrayList<>();

        if (userDetails != null) {
            clientService.findByEmail(userDetails.getUsername()).ifPresent(client -> {
                List<Event> recommended = eventRecommendationService.getRecommendedEvents(client, 5);
                recommendedDTOs.addAll(
                        recommended.stream()
                                .map(event -> eventMapper.toBasicDTO(event))
                                .toList());
            });
        }

        IndexResponseDTO indexDto = new IndexResponseDTO(topDTOs, bottomDTOs, artistsDTOs, recommendedDTOs);
        return ResponseEntity.ok(indexDto);
    }

}
