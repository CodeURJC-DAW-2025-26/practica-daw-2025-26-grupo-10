package es.tickethub.tickethub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/admin")
    public String getAdmin() {

        return "admin/admin";
    }
    
    @GetMapping("/statistics")
    public String getStatistics() {

        return "admin/statistics";
    }
    
}
