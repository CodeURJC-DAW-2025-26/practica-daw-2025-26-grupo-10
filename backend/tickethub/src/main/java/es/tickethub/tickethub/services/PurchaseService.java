package es.tickethub.tickethub.services;

import java.math.BigDecimal;
import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.AreaBreakType;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.repositories.ClientRepository;
import es.tickethub.tickethub.repositories.PurchaseRepository;

@Service
public class PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TicketService ticketService;

    @Autowired
    ZoneService zoneService;

    @Autowired
    EventService eventService;
    
    @Autowired
    private QrService qrService;

    /**
     * Processes a complete purchase workflow:
     * 1. Resolves client (new or existing).
     * 2. Calculates and validates total price.
     * 3. Configures session and links tickets.
     * 4. Persists the entire tree.
     */
    @Transactional
    public Purchase processPurchase(Long eventId, String totalPrice, List<Long> zoneIds, Long sessionId, String email) {
        Event event = eventService.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));

        Client client = clientService.getClientRepository().findByEmail(email)
                .orElse(new Client(email, "", "", "", "", 0, 0, BigDecimal.ZERO, null, null, null));
        clientService.getClientRepository().save(client);

        Purchase purchase = new Purchase();
        purchase.setClient(client);

        String cleanPrice = totalPrice.replace("€", "").replace(",", ".").trim();
        purchase.setTotalPrice(new BigDecimal(cleanPrice));

        event.getSessions().stream()
                .filter(s -> s.getSessionID().equals(sessionId))
                .findFirst()
                .ifPresent(purchase::setSession);

        for (Long zoneId : zoneIds) {
            Zone zone = zoneService.findById(zoneId);
            Ticket ticket = new Ticket();
            ticket.setZone(zone);
            ticket.setTicketPrice(zone.getPrice());
            ticket.setPurchase(purchase);
            purchase.getTickets().add(ticket);
        }

        return purchaseRepository.save(purchase);
    }
    
    /**
     * Retrieves all purchases not matter the client (for admin usage)
     */
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    /**
     * Retrieves all purchases belonging to a specific client
     * Useful for displaying the client's purchase history
     */
    @Transactional(readOnly = true)
    public Slice<Purchase> getPurchasesByClientEmail(String loggedEmail, int pageNumber) {
        if (!clientRepository.existsByEmail(loggedEmail)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "session.date"));
        return purchaseRepository.findByClient_Email(loggedEmail, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByPurchase(Long purchaseID, String loggedEmail) {
        Purchase purchase = purchaseRepository.findById(purchaseID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));
        if (!purchase.getClient().getEmail().equals(loggedEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para ver esta información");
        }
        return purchase.getTickets();
    }

    /**
     * Retrieves a specific purchase by its ID and ensures it belongs to the given
     * client
     * Throws a NOT_FOUND exception if the purchase does not exist or does not
     * belong to the client
     */
    public Purchase getPurchaseByIdAndClient(Long purchaseId, Client client) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findByPurchaseIDAndClient(purchaseId, client);
        if (!optionalPurchase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada");
        }
        return optionalPurchase.get();
    }

    /**
     * Deletes a purchase by its ID
     * If the purchase does not exist, throws a NOT_FOUND exception
     * All associated tickets are automatically deleted due to cascade settings
     */
    @Transactional
    public void deletePurchase(Long purchaseId) {
        if (!purchaseRepository.existsById(purchaseId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada");
        }
        purchaseRepository.deleteById(purchaseId);
    }

    public Purchase getPurchaseById(Long purchaseId) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(purchaseId);
        if (!optionalPurchase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada");
        }
        return optionalPurchase.get();
    }

    /**
     * Generates a PDF containing ticket details and QR codes.
     * Includes security validation to prevent unauthorized PDF downloads.
     */
    public byte[] generateTicketsPdf(Long purchaseId, String userEmail) throws Exception {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));

        if (!purchase.getClient().getEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("TICKETHUB - PURCHASE SUMMARY").setBold().setFontSize(18));
        document.add(new Paragraph("Event: " + purchase.getSession().getEvent().getName()).setBold());
        document.add(new Paragraph("Date: " + purchase.getSession().getFormattedDate()).setUnderline());

        int i = 1;
        for (Ticket ticket : purchase.getTickets()) {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.add(new Paragraph("TICKET " + i).setBold().setFontSize(18));
            document.add(new Paragraph("Zone: " + ticket.getZone().getName()));
            document.add(new Paragraph("Code: " + ticket.getCode()));
            document.add(new Paragraph("Price: " + ticket.getTicketPrice() + "€"));

            byte[] qrBytes = qrService.generateQR(ticket.getCode());
            Image qrImage = new Image(ImageDataFactory.create(qrBytes)).setWidth(200);

            document.add(new Paragraph("Show this QR code at the entrance:"));
            document.add(qrImage);
            i++;
        }
        document.close();
        
        return baos.toByteArray();
    }
}
