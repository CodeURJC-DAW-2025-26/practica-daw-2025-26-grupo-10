package es.tickethub.tickethub.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.services.ClientService;

@Controller
public class AdminUserController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<Client> clients = clientService.getClientRepository().findAll()
                .stream()
                .filter(c -> !c.getAdmin())
                .collect(Collectors.toList());

        model.addAttribute("clients", clients);
        return "admin/users/admin_users";
    }


    @GetMapping("/admin/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        Client client = clientService.getClientRepository().findById(id).orElseThrow();
        model.addAttribute("client", client);
        return "admin/users/edit_users";
    }

    @PostMapping("/admin/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute Client formClient) {
        clientService.updateUser(id, formClient);
        return "redirect:/admin/users";
    }
}