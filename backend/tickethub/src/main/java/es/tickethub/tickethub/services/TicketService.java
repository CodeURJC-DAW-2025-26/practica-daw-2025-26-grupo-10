package es.tickethub.tickethub.services;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.image.ImageDataFactory;


import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
public class TicketService {

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
}
