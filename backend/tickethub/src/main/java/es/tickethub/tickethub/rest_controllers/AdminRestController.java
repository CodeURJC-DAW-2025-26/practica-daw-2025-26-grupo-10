package es.tickethub.tickethub.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.AdminDashboardDTO;
import es.tickethub.tickethub.dto.AdminStatisticsDTO;
import es.tickethub.tickethub.services.AdminService;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> dashBoard() {
        return ResponseEntity.ok(adminService.getDashboard());
    }

    @GetMapping("/statistics")
    public ResponseEntity<AdminStatisticsDTO> statistics() {
        return ResponseEntity.ok(adminService.getStatistics());
    }
}
