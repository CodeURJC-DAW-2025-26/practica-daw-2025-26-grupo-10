package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.services.ClientService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("clients", clientService.getNonAdminClients());
        return "admin/users/admin_users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.getClientById(id));
        return "admin/users/edit_users";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute Client formClient) {
        clientService.updateUser(id, formClient);
        return "redirect:/admin/users";
    }
}