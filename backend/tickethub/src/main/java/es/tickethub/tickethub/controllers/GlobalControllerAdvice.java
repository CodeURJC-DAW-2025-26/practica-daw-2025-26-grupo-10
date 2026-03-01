package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.tickethub.tickethub.entities.User;
import es.tickethub.tickethub.repositories.UserRepository;
import java.security.Principal;
import java.util.Optional;

/**
 * Alert for all controllers -> before loading any page, execute this code
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserRepository userRepository;

    /**
     * Any method inside @ControllerAdvice that is @ModelAttribute will be executed
     * before loading any page
     */
    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        // Is logged
        if (principal != null) {
            model.addAttribute("isLogged", true);

            String email = principal.getName();

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("loggedUserID", user.getUserID());
                // Checking if it's not null and then if it's true
                boolean admin = (user.getAdmin() != null && user.getAdmin());
                model.addAttribute("isAdmin", admin);
            }
            // Is NOT logged
        } else {
            model.addAttribute("isLogged", false);
            model.addAttribute("isAdmin", false);
        }
    }
}