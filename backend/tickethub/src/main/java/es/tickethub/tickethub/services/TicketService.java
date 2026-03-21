package es.tickethub.tickethub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.repositories.TicketRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
public class TicketService {

    @Autowired
    private PdfGeneratorService pdfGenerator;

    @Autowired private TicketRepository ticketRepository;
    
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

    public byte[] generateTicketsPdf(Purchase purchase) throws Exception {
        return pdfGenerator.generatePurchasePdf(purchase);
    }

    /**
     * Retrieves a ticket entity by id.
     */
    public Ticket findById(Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    /**
     * Persists a ticket entity into the database.
     */
    public Ticket save(Ticket ticket){
        return ticketRepository.save(ticket);
    }
}