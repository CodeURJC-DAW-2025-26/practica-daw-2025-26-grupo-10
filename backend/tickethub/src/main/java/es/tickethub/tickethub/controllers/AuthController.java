package es.tickethub.tickethub.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.services.ClientService;

@Controller
@RequestMapping("/public")
public class AuthController {

    @Autowired
    private ClientService clientService;

    /** Login */
    @GetMapping("/login")
    public String showLogin(Model model, @RequestParam(required = false) String error, Principal principal) {
        if (principal != null) return "redirect:/public/selector";
        if (error != null) model.addAttribute("error", true);
        return "public/login";
    }

    /** Signup */
    @GetMapping("/signup")
    public String showSignup(Principal principal) {
        if (principal != null) return "redirect:/public/selector";
        return "public/signup";
    }

    /** Registro de cliente */
    @PostMapping("/registration")
    public String registerClient(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String surname,
            @RequestParam String password,
            @RequestParam String passWordConfirmation,
            @RequestParam String username,
            RedirectAttributes redirectAttributes) {

        try {
            clientService.registerClient(name, email, surname, password, passWordConfirmation, username);
            redirectAttributes.addFlashAttribute("success", "¡Cuenta creada! Ya puedes iniciar sesión.");
            return "redirect:/public/login";

        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", e.getReason());
            return "redirect:/public/signup";
        }
    }
}