package es.tickethub.tickethub.controllers;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import es.tickethub.tickethub.entities.Client;

import es.tickethub.tickethub.services.ClientService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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


    @PostMapping("/registration")//TODO:Conectar con la parte de security
    public String registeClient(Model model, @RequestParam String name, @RequestParam String email,@RequestParam String surname,
        @RequestParam String password, @RequestParam String passWordConfirmation,RedirectAttributes redirectAttributes) {
        try {
            clientService.registeClient(name, email,surname, password, passWordConfirmation);
            return "/clients/main";//TODO: devolver a la pantalla de inicio
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error",e.getReason());
            return "redirect:/clients/registration";
        }
    }
    
    @GetMapping("/profile/{userId}")
    public String getClientOverview(Model model, @PathVariable Long userId) {
        Client clientLogged = clientService.getClientById(userId);
        
        model.addAttribute("clientLogged",clientLogged);
        return "/user/profile";
    }

    // EDIT CLIENT INFORMACION
    @GetMapping("/profile/{userId}/edit")
    public String getClientData(@PathVariable Long userId,Model model) {
        Client clientLogged = clientService.getClientById(userId);
        
        model.addAttribute("clientLogged",clientLogged);
        return "/user/edit_profile";
    }

    @PostMapping("/profile/{userId}/edit")
    //TODO: futuramente con react usaremos el @RequestBody, por ahora usamos @ModelAttribute
    public String uptadeClientData(@Valid @ModelAttribute Client client,
        //BindingResult bindingResult,
        RedirectAttributes redirectAttributes,@RequestParam MultipartFile imageFile,
        @PathVariable Long userId){
        try {
            clientService.updateClient(userId, client,imageFile);
            redirectAttributes.addFlashAttribute("success","Perfil actualizado correctamente");
            return "redirect:/clients/profile/" + userId + "/edit";
        } catch (ObjectOptimisticLockingFailureException e) {
            redirectAttributes.addFlashAttribute("error","Alguien ha modificado el perfil mientras lo editaba");
            return "redirect:/clients/profile/" + userId + "/edit";
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud");
            return "redirect:/clients/profile/" + userId + "/edit";
        }
        
    }
    

    //CHANGE PASSWORD
    @GetMapping("/profile/{userId}/password")
    public String getPasswordScreen(Model model,@PathVariable Long userId) {
        model.addAttribute("userID",userId);
        return "/user/change_password";
    }

    @PostMapping("/profile/{userId}/password")
    public String changePassword(@RequestParam String oldPassword,@RequestParam String newPassword,
        @RequestParam String newPasswordConfirmation,RedirectAttributes redirectAttributes,
        @PathVariable Long userId) {
        try {
            clientService.changePassword(userId, oldPassword, newPassword, newPasswordConfirmation);
            redirectAttributes.addFlashAttribute("success","Contraseña actualizada correctamente");
            return "redirect:/clients/profile/"+userId+"/password";    
        }catch(ObjectOptimisticLockingFailureException e){
            redirectAttributes.addFlashAttribute("error","Hubo un conflicto procesando su solicitud. Por favor intente de nuevo.");
            return "redirect:/clients/profile/"+userId+"/password";
        }catch(ResponseStatusException e){
            redirectAttributes.addFlashAttribute("error",e.getReason());
             return "redirect:/clients/profile/"+userId+"/password";
        }
    }
}
