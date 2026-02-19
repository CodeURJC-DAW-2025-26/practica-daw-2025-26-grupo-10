package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.services.PurchaseService;

import java.util.List;

@Controller
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    /**
     * Retrieves all purchases for a given client email.
     * Stateless: the client is identified via request parameter.
     */
    @GetMapping
    public String listPurchases(@RequestParam("email") String clientEmail, Model model) {
        // Create a temporary Client object with the given email
        Client client = new Client();
        client.setEmail(clientEmail);

        List<Purchase> purchases = purchaseService.getPurchasesByClient(client);
        model.addAttribute("purchases", purchases);
        return "purchases"; // template to show purchase history
    }

    /**
     * Shows the details of a specific purchase for a given client email.
     * Includes all tickets associated with the purchase.
     */
    @GetMapping("/{id}")
    public String showPurchaseDetails(@PathVariable Long id, @RequestParam("email") String clientEmail, Model model) {
        // Create a temporary Client object with the given email
        Client client = new Client();
        client.setEmail(clientEmail);

        // Retrieve the purchase ensuring it belongs to the client
        Purchase purchase = purchaseService.getPurchaseByIdAndClient(id, client);

        // If not found, an exception is already thrown by the service (NOT_FOUND)
        model.addAttribute("purchase", purchase);
        model.addAttribute("tickets", purchase.getTickets()); // tickets inside this purchase
        return "purchase-details"; // template to show purchase -> tickets
    }
}
