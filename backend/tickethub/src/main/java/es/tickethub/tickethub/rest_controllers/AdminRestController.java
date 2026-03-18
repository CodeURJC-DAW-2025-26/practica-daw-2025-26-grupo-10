package es.tickethub.tickethub.rest_controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.AdminDashboardDTO;
import es.tickethub.tickethub.dto.AdminStatisticsDTO;
import es.tickethub.tickethub.repositories.PurchaseRepository;
import es.tickethub.tickethub.services.AdminService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private final PurchaseRepository purchaseRepository;
    private final AdminService adminService;

    public AdminRestController(PurchaseRepository purchaseRepository, AdminService adminService) {
        this.purchaseRepository = purchaseRepository;
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> dashBoard() {
        int activeEvents = adminService.eventsActive();
        long numberTickets = adminService.getNumberTickets();
        long numberUsers = adminService.getNumberUsers();
        long numberAdmins = adminService.getNumberAdmins();
        AdminDashboardDTO dashboardDTO = new AdminDashboardDTO(activeEvents,numberTickets,numberUsers,numberAdmins);
        return ResponseEntity.ok(dashboardDTO);
    }

    @GetMapping("/statistics")
    public ResponseEntity<AdminStatisticsDTO> statistics() {
        List<Object[]> monthEventData = purchaseRepository.getTicketsByMonthAndEvent();
        List<Object[]> ranking = purchaseRepository.getRankingByEvent();
        List<Object[]> evolution = purchaseRepository.getTotalTicketsEvolution();

        List<String> rankingLabels = ranking.stream()
                .map(d -> String.valueOf(d[0]))
                .collect(Collectors.toList());
                
        List<Number> rankingValues = ranking.stream()
                .map(d -> (Number) d[1])
                .collect(Collectors.toList());

        List<String> evolutionLabels = evolution.stream()
                .map(d -> String.valueOf(d[0]))
                .collect(Collectors.toList());
                
        List<Number> evolutionValues = evolution.stream()
                .map(d -> (Number) d[1])
                .collect(Collectors.toList());

        AdminStatisticsDTO responseDTO = new AdminStatisticsDTO(
                monthEventData,
                rankingLabels,
                rankingValues,
                evolutionLabels,
                evolutionValues
        );

        return ResponseEntity.ok(responseDTO);
    }
    
    
}
