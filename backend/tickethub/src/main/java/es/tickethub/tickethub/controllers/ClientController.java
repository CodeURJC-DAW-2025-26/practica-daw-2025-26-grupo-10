package es.tickethub.tickethub.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.services.ClientService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/profile")
    public String getClientOverview(Model model, Principal principal) {
        Client clientLogged = clientService.getClientByEmail(principal.getName());

        model.addAttribute("useID", clientLogged.getUserID());
        model.addAttribute("clientLogged", clientLogged);

        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String getClientData(Principal principal, Model model) {
        Client clientLogged = clientService.getClientByEmail(principal.getName());

        model.addAttribute("clientLogged", clientLogged);
        return "user/edit_profile";
    }

    @PostMapping("/profile/edit")
    public String uptadeClientData(@Valid @ModelAttribute Client client,
            RedirectAttributes redirectAttributes,
            @RequestParam MultipartFile imageFile,
            Principal principal,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Datos inválidos. Comprueba que la edad sea positiva y los campos correctos.");
            return "redirect:/clients/profile/edit";
        }

        try {
            clientService.updateClient(principal.getName(), client, imageFile);
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente");
            return "redirect:/clients/profile/edit";

        } catch (ObjectOptimisticLockingFailureException e) {
            redirectAttributes.addFlashAttribute("error", "Alguien ha modificado el perfil mientras lo editaba");
            return "redirect:/clients/profile/edit";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud");
            return "redirect:/clients/profile/edit";
        }
    }

    @GetMapping("/profile/password")
    public String getPasswordScreen(Model model) {
        return "user/change_password";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String newPasswordConfirmation,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        try {
            clientService.changePassword(principal.getName(), oldPassword, newPassword, newPasswordConfirmation);
            redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente");
            return "redirect:/clients/profile/password";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clients/profile/password";
        }
    }
}