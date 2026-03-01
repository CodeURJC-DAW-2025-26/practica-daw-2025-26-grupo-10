package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.repositories.ClientRepository;
import es.tickethub.tickethub.entities.Event;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import es.tickethub.tickethub.services.DiscountService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.PurchaseService;
import es.tickethub.tickethub.services.QrService;
import es.tickethub.tickethub.services.TicketService;
import es.tickethub.tickethub.services.ZoneService;
import jakarta.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.io.image.ImageDataFactory;

@Controller
@RequestMapping("/purchases")
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

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private QrService qrService;

    /**
     * Retrieves the first page of purchases for the currently logged-in user.
     */
    @GetMapping("/me")
    public String getPurchasesByClientEmail(Principal principal, Model model) {
        String loggedEmail = principal.getName();
        Slice<Purchase> purchasesSlice = purchaseService.getPurchasesByClientEmail(loggedEmail, 0);
        model.addAttribute("purchases", purchasesSlice.getContent());
        model.addAttribute("hasNext", purchasesSlice.hasNext());
        model.addAttribute("nextPage", 1);
        return "user/purchases";
    }

    /**
     * Endpoint for AJAX requests to load subsequent pages of purchases.
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
     * Loads a fragment containing ticket details for a specific purchase.
     */
    @GetMapping("/{purchaseId}/tickets")
    public String getPurchaseTickets(@PathVariable Long purchaseId, Model model, Principal principal) {
        String loggedEmail = principal.getName();
        List<Ticket> tickets = purchaseService.getTicketsByPurchase(purchaseId, loggedEmail);
        model.addAttribute("tickets", tickets);
        return "user/purchase_details_fragment";
    }

    /**
     * Displays the purchase selection page (Zones, Sessions, etc.).
     * Crucial: It checks if the user is 'logged in' to inform the Frontend.
     */
    @GetMapping("/select/{eventID}")
    public String showPurchaseFromEvent(@PathVariable Long eventID, Model model, Principal principal) {
        Event event = eventService.findById(eventID);
        List<Zone> zones = zoneService.findAll();
        List<Discount> discounts = discountService.getAllDiscounts();

        // Inform the JS if the user is authenticated to handle redirects manually
        model.addAttribute("logged", principal != null);

        model.addAttribute("event", event);
        model.addAttribute("zones", zones);
        model.addAttribute("discounts", discounts);
        model.addAttribute("totalPrice", BigDecimal.ZERO);

        return "public/purchase";
    }

    /**
     * Processes the purchase submission.
     * Uses CascadeType.ALL to save tickets along with the purchase.
     */
    @PostMapping("/save")
    public String savePurchase(@RequestParam Long eventId, @RequestParam Long sessionId,
            @RequestParam String totalPrice, @RequestParam List<Long> zoneIds, Principal principal, Model model) {

        Event event = eventService.findById(eventId);
        String email = principal.getName();
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        Purchase purchase = new Purchase();
        purchase.setClient(client);

        // Clean and parse the total price string coming from the frontend
        String cleanPrice = totalPrice.replace("€", "").replace(",", ".").trim();
        purchase.setTotalPrice(new BigDecimal(cleanPrice));

        // Assign the selected session using Java Streams
        event.getSessions().stream()
                .filter(s -> s.getSessionID().equals(sessionId))
                .findFirst()
                .ifPresent(purchase::setSession); // setSession is a setter

        // Create tickets and establish the bidirectional relationship
        for (Long zoneId : zoneIds) {
            Zone zone = zoneService.findById(zoneId);

            Ticket ticket = new Ticket();
            ticket.setZone(zone);
            ticket.setTicketPrice(zone.getPrice());
            ticket.setPurchase(purchase); // Set the owner side

            purchase.getTickets().add(ticket); // Add to the collection for cascading
        }

        // Save the purchase; tickets are persisted automatically due to CascadeType.ALL
        Purchase savedPurchase = purchaseService.createPurchase(purchase);

        model.addAttribute("event", event);
        model.addAttribute("purchase", savedPurchase);

        return "public/confirmation";
    }

    /**
     * Generates a PDF file containing all tickets and their corresponding QR codes.
     */
    @GetMapping("/download/{purchaseId}")
    public void downloadTickets(@PathVariable Long purchaseId, HttpServletResponse response, Principal principal)
            throws Exception {
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);

        // Security check: only the owner of the purchase can download the PDF
        if (!purchase.getClient().getEmail().equals(principal.getName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=TicketHub_Order_" + purchaseId + ".pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add Header Info
        document.add(new Paragraph("TICKETHUB - PURCHASE SUMMARY").setBold().setFontSize(18));
        document.add(new Paragraph("Event: " + purchase.getSession().getEvent().getName()).setBold());
        document.add(new Paragraph("Date: " + purchase.getSession().getFormattedDate()).setUnderline());

        // Generate a page for each ticket with its unique QR code
        List<Ticket> tickets = purchase.getTickets();
        int i = 1;
        for (Ticket ticket : tickets) {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.add(new Paragraph("TICKET " + i).setBold().setFontSize(18));
            document.add(new Paragraph("Zone: " + ticket.getZone().getName()));
            document.add(new Paragraph("Code: " + ticket.getCode()));
            document.add(new Paragraph("Price: " + ticket.getTicketPrice() + "€"));

            // Generate QR code using the ticket's unique UUID code
            byte[] qrBytes = qrService.generateQR(ticket.getCode());
            Image qrImage = new Image(ImageDataFactory.create(qrBytes));
            qrImage.setWidth(200);

            document.add(new Paragraph("Show this QR code at the entrance:"));
            document.add(qrImage);
            i++;
        }
        document.close();
    }
}
