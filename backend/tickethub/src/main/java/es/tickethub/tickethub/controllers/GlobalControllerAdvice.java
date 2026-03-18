package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import es.tickethub.tickethub.services.UserService;
import java.security.Principal;


@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("isLogged", true);
            
            userService.findByEmail(principal.getName()).ifPresent(user -> {
                model.addAttribute("loggedUserID", user.getUserID());
                model.addAttribute("isAdmin", userService.isAdmin(user));
            });
        } else {
            model.addAttribute("isLogged", false);
            model.addAttribute("isAdmin", false);
        }
    }
}
