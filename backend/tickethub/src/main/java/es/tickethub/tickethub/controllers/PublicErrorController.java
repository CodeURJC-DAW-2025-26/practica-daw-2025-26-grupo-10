package es.tickethub.tickethub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public/error")
public class PublicErrorController {

    @GetMapping("/403")
    public String show403Error(Model model) {
        model.addAttribute("messageError", "No tienes permiso para acceder a este recurso.");
        return "error/403";
    }

    @GetMapping("/404")
    public String show404Error() {
        return "error/404";
    }

    @GetMapping("/500")
    public String show500Error() {
        return "error/500";
    }
}