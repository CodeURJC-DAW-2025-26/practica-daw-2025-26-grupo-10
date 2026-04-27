package es.tickethub.tickethub.services;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;

@Service
public class PdfGeneratorService {

    @Autowired
    private QrService qrService;

    public byte[] generatePurchasePdf(Purchase purchase) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        // Fuentes
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        // Cabecera principal
        document.add(new Paragraph("TICKETHUB - RESUMEN DE COMPRA", titleFont));
        document.add(new Paragraph("Evento: " + purchase.getSession().getEvent().getName()));
        document.add(new Paragraph("Fecha: " + purchase.getSession().getFormattedDate()));
        document.add(new Paragraph("Comprador: " + purchase.getClient().getEmail()));
        document.add(new Paragraph("Total: " + purchase.getTotalPrice() + " €"));

        int i = 1;
        for (Ticket ticket : purchase.getTickets()) {
            document.newPage(); // Une page per ticket
            
            document.add(new Paragraph("ENTRADA #" + i, titleFont));
            document.add(new Paragraph("-------------------------------------------"));
            document.add(new Paragraph("Zona: " + ticket.getZone().getName(), boldFont));
            document.add(new Paragraph("Código: " + ticket.getCode()));
            document.add(new Paragraph("Precio: " + ticket.getTicketPrice() + " €"));

            // Generate and add QR
            byte[] qrBytes = qrService.generateQR(ticket.getCode());
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.scaleToFit(200, 200);
            qrImage.setAlignment(Element.ALIGN_CENTER);

            document.add(new Paragraph("Muestra este código en la entrada:"));
            document.add(qrImage);
            i++;
        }

        document.close();
        return baos.toByteArray();
    }
}