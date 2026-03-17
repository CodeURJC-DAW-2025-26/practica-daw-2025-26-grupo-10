package es.tickethub.tickethub.controllers;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.tickethub.tickethub.dto.PurchaseBasicDTO;
import es.tickethub.tickethub.dto.TicketBasicDTO;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.mappers.DiscountMapper;
import es.tickethub.tickethub.mappers.EventMapper;
import es.tickethub.tickethub.mappers.PurchaseMapper;
import es.tickethub.tickethub.mappers.TicketMapper;
import es.tickethub.tickethub.mappers.ZoneMapper;
import es.tickethub.tickethub.services.DiscountService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.PurchaseService;

@Controller
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private EventService eventService;
    @Autowired
    private DiscountService discountService;

    @Autowired private EventMapper eventMapper;
    @Autowired private ZoneMapper zoneMapper;
    @Autowired private DiscountMapper discountMapper;
    @Autowired private PurchaseMapper purchaseMapper;   
    @Autowired private TicketMapper ticketMapper;    

    /**
     * Fetches the initial page of user purchase history.
     */
    @GetMapping("/me")
    public String getPurchasesByClientEmail(Principal principal, Model model) {
        String loggedEmail = principal.getName();
        Slice<Purchase> purchasesSlice = purchaseService.getPurchasesByClientEmail(loggedEmail, 0);
        
        List<PurchaseBasicDTO> dtos = purchasesSlice.getContent().stream()
                .map(purchaseMapper::toBasicDTO)
                .toList();

        model.addAttribute("purchases", dtos);
        model.addAttribute("hasNext", purchasesSlice.hasNext());
        model.addAttribute("nextPage", 1);
        return "user/purchases";
    }

    /**
     * Handles AJAX request for infinite scroll/pagination in the UI.
     */
    @GetMapping("/me/more")
    public String loadMorePurchases(Principal principal, @RequestParam int pageNumber, Model model) {
        String loggedEmail = principal.getName();
        Slice<Purchase> purchasesSlice = purchaseService.getPurchasesByClientEmail(loggedEmail, pageNumber);

        model.addAttribute("purchases", purchasesSlice.getContent());
        model.addAttribute("hasNext", purchasesSlice.hasNext());
        model.addAttribute("nextPage", pageNumber + 1);
        return "user/purchase_items";
    }

    /**
     * Returns a Mustache fragment with ticket details for dynamic UI loading.
     */
    @GetMapping("/{purchaseId}/tickets")
    public String getPurchaseTickets(@PathVariable Long purchaseId, Model model, Principal principal) {
        String loggedEmail = principal.getName();
        
        List<Ticket> tickets = purchaseService.getTicketsByPurchase(purchaseId, loggedEmail);
        List<TicketBasicDTO> ticketDTOs = tickets.stream()
                .map(ticketMapper::toBasicDTO)
                .toList();
        
        model.addAttribute("tickets", ticketDTOs);
        
        return "user/purchase_details_fragment";
    }

    /**
     * Renders the purchase selection page for a specific event.
     */
    @GetMapping("/select/{eventID}")
    public String showPurchaseFromEvent(@PathVariable Long eventID, Model model, Principal principal) {
        Optional<Event> optionalEvent = eventService.findById(eventID);
        
        if (optionalEvent.isEmpty()) {
            return "redirect:/public/events";
        }

        Event event = optionalEvent.get();

        model.addAttribute("event", eventMapper.toDTO(event));

        model.addAttribute("zones", event.getZones().stream()
                .map(zoneMapper::toDTO)
                .toList());

        model.addAttribute("discounts", discountService.getAllDiscounts().stream()
                .map(discountMapper::toDTO)
                .toList());

        model.addAttribute("logged", principal != null);
        model.addAttribute("totalPrice", BigDecimal.ZERO);

        return "public/purchase";
    }
}
