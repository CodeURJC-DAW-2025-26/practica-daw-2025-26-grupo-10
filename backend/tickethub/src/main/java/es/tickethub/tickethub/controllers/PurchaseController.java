package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.entities.Event;

import java.math.BigDecimal;
import java.util.List;

import es.tickethub.tickethub.services.DiscountService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.PurchaseService;
import es.tickethub.tickethub.services.ZoneService;


@Controller
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ZoneService zoneService;
    
    @Autowired
    private DiscountService discountService;
    /**
     * Retrieves 10 purchases for a given client ID.
     * Stateless: the client is identified via request parameter.
     */
    @GetMapping("/me/{userId}")
    public String listPurchasesByClientId(@PathVariable Long userId ,Model model) {
        // TODO: Cuando tengamos Spring Security, sacaremos el ID/Email del usuario logueado
        // Long clientId = authentication.getName();
        Slice<Purchase> purchasesSlice = purchaseService.getPurchasesByClientId(userId,0);
        model.addAttribute("userID",userId);
        model.addAttribute("purchases", purchasesSlice.getContent());
        model.addAttribute("hasNext",purchasesSlice.hasNext());
        model.addAttribute("nextPage",1);
        return "user/purchases";
    }

    /**
     * This is for Ajax to load the next 10 purchases
     */
    @GetMapping("/me/{userId}/more")
    public String loadMorePurchases(@PathVariable Long userId,@RequestParam int pageNumber,Model model) {
        Slice<Purchase> purchasesSlice = purchaseService.getPurchasesByClientId(userId, pageNumber);
        model.addAttribute("purchases",purchasesSlice.getContent());
        model.addAttribute("hasNext", purchasesSlice.hasNext());
        model.addAttribute("nextPage", pageNumber + 1);
        return "user/purchase_items";
    }

    @GetMapping("/{purchaseId}/tickets")
    public String getPurchaseTickets(@PathVariable Long purchaseId, Model model) {
        List<Ticket> tickets = purchaseService.getTicketsByPurchase(purchaseId);
        model.addAttribute("tickets", tickets);
        return "user/purchase_details_fragment";
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
}
