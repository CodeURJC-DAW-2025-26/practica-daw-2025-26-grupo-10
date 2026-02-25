package es.tickethub.tickethub.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.tickethub.tickethub.entities.Client;

import es.tickethub.tickethub.services.ClientService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;


@Controller
@RequestMapping("/clients")
public class ClientController {
    //TODO: Cuando tengamos lo de security tendremos que sacar la información del authentication
    //aqui se encuentra lo que es el id del usuario o el email
    //lo que nosotros le pongamos dentro de lo que es la creación del token
    @Autowired
    private ClientService clientService;
    
    @GetMapping("/profile")
    public String getClientOverview(Model model, Principal principal) {
        String loggedEmail = principal.getName();
        Client clientLogged = clientService.getClientByEmail(loggedEmail);
        model.addAttribute("useID", clientLogged.getUserID());
        model.addAttribute("clientLogged",clientLogged);
        return "user/profile";
    }

    // EDIT CLIENT INFORMACION
    @GetMapping("/profile/edit")
    public String getClientData(Principal principal,Model model) {
        String loggedEmail = principal.getName();
        Client clientLogged = clientService.getClientByEmail(loggedEmail);
        
        model.addAttribute("clientLogged",clientLogged);
        return "user/edit_profile";
    }

    @PostMapping("/profile/edit")
    //TODO: futuramente con react usaremos el @RequestBody
    public String uptadeClientData(@Valid @ModelAttribute Client client,
        //BindingResult bindingResult,
        RedirectAttributes redirectAttributes,@RequestParam MultipartFile imageFile,
        Principal principal, BindingResult bindingResult){
        
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Datos inválidos. Comprueba que la edad sea positiva y los campos correctos.");
            return "redirect:/clients/profile/edit";
        }
        try {
            String loggedEmail = principal.getName();
            clientService.updateClient(loggedEmail, client,imageFile);
            redirectAttributes.addFlashAttribute("success","Perfil actualizado correctamente");
            return "redirect:/clients/profile/edit";
        } catch (ObjectOptimisticLockingFailureException e) {
            redirectAttributes.addFlashAttribute("error","Alguien ha modificado el perfil mientras lo editaba");
            return "redirect:/clients/profile/edit";
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud");
            return "redirect:/clients/profile/edit";
        }
        
    }
    

    //CHANGE PASSWORD
    @GetMapping("/profile/password")
    public String getPasswordScreen() {
        return "user/change_password";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String oldPassword,@RequestParam String newPassword,
        @RequestParam String newPasswordConfirmation,RedirectAttributes redirectAttributes,
        Principal principal) {
        try {
            String loggedEmail = principal.getName();
            clientService.changePassword(loggedEmail, oldPassword, newPassword, newPasswordConfirmation);
            redirectAttributes.addFlashAttribute("success","Contraseña actualizada correctamente");
            return "redirect:/clients/profile/password";    
        }catch(ObjectOptimisticLockingFailureException e){
            redirectAttributes.addFlashAttribute("error","Hubo un conflicto procesando su solicitud. Por favor intente de nuevo.");
            return "redirect:/clients/profile/password";
        }catch(ResponseStatusException e){
            redirectAttributes.addFlashAttribute("error",e.getReason());
             return "redirect:/clients/profile/password";
        }
    }
    
    

}