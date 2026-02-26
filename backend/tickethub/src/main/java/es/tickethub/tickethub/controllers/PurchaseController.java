package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CsrfToken;
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
import es.tickethub.tickethub.services.ZoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;


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

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private QrService qrService;
    /**
     * Retrieves 10 purchases for a given client ID.
     * Stateless: the client is identified via request parameter.
     */
    @GetMapping("/me")
    public String getPurchasesByClientEmail(Principal principal ,Model model) {
        // TODO: Cuando tengamos Spring Security, sacaremos el ID/Email del usuario logueado
        // Long clientId = authentication.getName();
        String loggedEmail = principal.getName();
        Slice<Purchase> purchasesSlice = purchaseService.getPurchasesByClientEmail(loggedEmail,0);
        model.addAttribute("purchases", purchasesSlice.getContent());
        model.addAttribute("hasNext",purchasesSlice.hasNext());
        model.addAttribute("nextPage",1);
        return "user/purchases";
    }

    /**
     * This is for Ajax to load the next 10 purchases
     */
    @GetMapping("/me/more")
    public String loadMorePurchases(Principal principal,@RequestParam int pageNumber,Model model) {
        String loggedEmail = principal.getName();
        Slice<Purchase> purchasesSlice = purchaseService.getPurchasesByClientEmail(loggedEmail, pageNumber);
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

    /* needed to see all the information at the purchase html*/
    @GetMapping("{eventId}")
    public String showPurchaseFromEvent(@PathVariable Long eventID, Model model, HttpServletRequest request) {
        Event event = eventService.findById(eventID);
        List<Zone> zones = zoneService.findAll();
        List<Discount> discounts = discountService.getAllDiscounts();

        model.addAttribute("event", event);
        model.addAttribute("zones", zones);
        model.addAttribute("discounts", discounts);
        model.addAttribute("tickets", List.of());
        model.addAttribute("totalPrice", BigDecimal.ZERO);

        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            model.addAttribute("_csrf", token);
        }

        return "public/purchase";
    }

    @GetMapping("/confirmation/{eventId}")
    public String showConfirmation(@PathVariable Long eventId, @RequestParam(name = "total", defaultValue = "0") String total, Model model) {
        Event event = eventService.findById(eventId);
        model.addAttribute("event", event);
        
        Purchase tempPurchase = new Purchase();
        tempPurchase.setTotalPrice(new BigDecimal(total));
        tempPurchase.setPurchaseID(0L); 
        model.addAttribute("purchase", tempPurchase);
        
        return "public/confirmation";
    }

    @PostMapping("/save")
    public String savePurchase(@RequestParam Long eventId, @RequestParam Long sessionId,@RequestParam String totalPrice, Principal principal, Model model) {
        Event event = eventService.findById(eventId);
        String email = principal.getName();
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Purchase purchase = new Purchase();
        
        String cleanPrice = totalPrice.replace("€", "").replace(",", ".").trim();
        purchase.setTotalPrice(new BigDecimal(cleanPrice));
        purchase.setClient(client);

        event.getSessions().stream()
            .filter(s -> s.getSessionID().equals(sessionId))
            .findFirst()
            .ifPresent(purchase::setSession);

        Purchase savedPurchase = purchaseService.createPurchase(purchase);

        model.addAttribute("event", event);
        model.addAttribute("purchase", savedPurchase);

        return "public/confirmation";
    }

    @GetMapping("/download/{purchaseId}")
    public void downloadTickets(@PathVariable Long purchaseId, HttpServletResponse response, Principal principal) throws Exception {
        
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        
        if (!purchase.getClient().getEmail().equals(principal.getName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=TicketHub_Order_" + purchaseId + ".pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("TICKETHUB - RESUMEN DE COMPRA").setBold().setFontSize(18));
        document.add(new Paragraph("Evento: " + purchase.getSession().getEvent().getName()));
        document.add(new Paragraph("Fecha: " + purchase.getSession().getFormattedDate()));
        document.add(new Paragraph("Cliente: " + purchase.getClient().getEmail()));
        document.add(new Paragraph("Total pagado: " + purchase.getTotalPrice() + "€"));
        document.add(new Paragraph("\n"));

        byte[] qrBytes = qrService.generateQR("TICKET-" + purchaseId);
        Image qrImage = new Image(ImageDataFactory.create(qrBytes));
        qrImage.setWidth(200);
        
        document.add(new Paragraph("Muestra este código QR en la entrada:"));
        document.add(qrImage);

        document.close();
    }
}
