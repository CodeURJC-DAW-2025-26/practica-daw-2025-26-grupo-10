package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.services.ClientService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/clients")
public class ClientController {
    //TODO: Cuando tengamos lo de security tendremos que sacar la información del authentication
    //aqui se encuentra lo que es el id del usuario o el email
    //lo que nosotros le pongamos dentro de lo que es la creación del token
    @Autowired
    private ClientService clientService;

    @GetMapping("/profile")
    public String getClientOverview(Model model) {
        Long idPrueba = 1L;
        Client clientLogged = clientService.getClientById(idPrueba);
        
        model.addAttribute("clientLogged",clientLogged);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String getClientData(Model model) {
        //obtenemos el id del usuario
        //Long userID = authentication.getName();
        Long idPrueba = 1L;
        Client clientLogged = clientService.getClientById(idPrueba);
        
        model.addAttribute("clientLogged",clientLogged);
        return "edit_profile";
    }

    @PostMapping("/profile/edit")
    //TODO: futuramente con react usaremos el @RequestBody, por ahora usamos @ModelAttribute
    public String uptadeClientData(@Valid @ModelAttribute Client client) {
        Long idPrueba = 1L;
        clientService.updateClient(idPrueba, client);
        return "redirect:/clients/profile";
    }
    
    @GetMapping("/profile/password")
    public String getPasswordScreen() {
        return "change_password";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String oldPassword,@RequestParam String newPassword,
        @RequestParam String newPasswordConfirmation,RedirectAttributes redirectAttributes) {
        Long idPrueba = 1L;
        try {
            clientService.changePassword(idPrueba, oldPassword, newPassword, newPasswordConfirmation);
            return "redirect:/clients/profile";    
        } catch (ResponseStatusException error) {
            redirectAttributes.addFlashAttribute("error",error.getReason());
            return "redirect:/clients/profile/password";
        }

        
    }
    
    

}