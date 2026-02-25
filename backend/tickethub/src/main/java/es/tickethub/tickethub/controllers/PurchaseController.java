package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.entities.Event;

import java.math.BigDecimal;
import java.util.List;

import es.tickethub.tickethub.services.DiscountService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.PurchaseService;
import es.tickethub.tickethub.services.TicketService;
import es.tickethub.tickethub.services.ZoneService;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ZoneService zoneService;
    
    @Autowired
    private DiscountService discountService;
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
    
    /* needed to see all the information at the purchase html*/
    @GetMapping("{eventId}")
    public String showPurchaseFromEvent(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);
        List<Zone> zones = zoneService.findAll();
        List<Discount> discounts = discountService.getAllDiscounts();

        model.addAttribute("event", event);
        model.addAttribute("zones", zones);
        model.addAttribute("discounts", discounts);
        model.addAttribute("tickets", List.of());
        model.addAttribute("totalPrice", BigDecimal.ZERO);

        return "public/purchase";
    }

    

    @GetMapping("/download/{purchaseId}")
    public ResponseEntity<byte[]> downloadTickets(@PathVariable Long purchaseId) throws Exception {

        Purchase purchase = purchaseService.getPurchaseById(purchaseId);

        byte[] pdf = ticketService.generateTicketsPdf(purchase);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=entradas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
