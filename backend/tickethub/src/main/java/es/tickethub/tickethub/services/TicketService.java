package es.tickethub.tickethub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.image.ImageDataFactory;

import es.tickethub.tickethub.dto.TicketDTO;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.mappers.TicketMapper;
import es.tickethub.tickethub.repositories.TicketRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
public class TicketService {

    @Autowired private TicketRepository ticketRepository;
    @Autowired private TicketMapper ticketMapper;
    
    /**
     * Internal helper to generate a PNG QR code as a byte array.
     * Uses ZXing to encode the input text into a BitMatrix.
     */
    private byte[] generateQR(String text) throws Exception {

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        return baos.toByteArray();
    }

    /**
     * Generates a multi-page PDF document for all tickets in a purchase.
     * Each page contains event details, ticket metadata, and a unique QR code.
     */
    public byte[] generateTicketsPdf(Purchase purchase) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        Event event = purchase.getSession().getEvent();

        for (Ticket ticket : purchase.getTickets()) {

            document.add(new Paragraph("ENTRADA")
                    .setBold()
                    .setFontSize(18));

            document.add(new Paragraph("Evento: " + event.getName()));
            document.add(new Paragraph("Zona: " + ticket.getZone().getName()));
            document.add(new Paragraph("Precio: " + ticket.getTicketPrice() + " €"));
            document.add(new Paragraph("Comprador: " + purchase.getClient().getEmail()));

            String qrText = "TICKET-" + ticket.getTicketID();
            byte[] qrImage = generateQR(qrText);

            Image qr = new Image(ImageDataFactory.create(qrImage))
                    .scaleToFit(120, 120);

            document.add(qr);

            document.add(new AreaBreak());
        }

        document.close();
        return baos.toByteArray();
    }

    /**
     * Retrieves and maps a ticket entity to a TicketDTO.
     */
    public TicketDTO findById(Long id) {
        return ticketRepository.findById(id).map(ticketMapper::toDTO).orElseThrow();
    }

    /**
     * Persists a TicketDTO into the database.
     */
    public TicketDTO save(TicketDTO ticketDTO){
        Ticket ticket = ticketMapper.toDomain(ticketDTO);
        return ticketMapper.toDTO(ticketRepository.save(ticket));
    }
}
