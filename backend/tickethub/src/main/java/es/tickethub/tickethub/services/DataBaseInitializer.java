package es.tickethub.tickethub.services;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.Admin;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.entities.Zone;

import es.tickethub.tickethub.repositories.ArtistRepository;
import es.tickethub.tickethub.repositories.DiscountRepository;
import es.tickethub.tickethub.repositories.EventRepository;
import es.tickethub.tickethub.repositories.PurchaseRepository;
import es.tickethub.tickethub.repositories.SessionRepository;
import es.tickethub.tickethub.repositories.UserRepository;
import es.tickethub.tickethub.repositories.ZoneRepository;

import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInitializer {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private byte[] loadImage(String imageName) {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("static/images/" + imageName)) {
            if (inputStream == null) {
                System.err.println("No se encontró la imagen: " + imageName);
                return new byte[0];
            }
            return inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static Blob convertToBlob(byte[] imageBytes) {
        try {
            return new SerialBlob(imageBytes);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* FUNCTION FOR ADD ALL THE IMAGES THAT WILL BE AT THE DATABASE*/
    public List <List <Image>> initializeImages() {
        
        List <List <Image>> allImages = new ArrayList<>();

        List <Image> artistImages = Arrays.asList(
            new Image("imagen1", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen2", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen3", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen4", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen5", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen6", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen7", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen8", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen9", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen10", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen11", convertToBlob(loadImage("database-diagram.png"))),
            new Image("imagen12", convertToBlob(loadImage("database-diagram.png")))
        );

        List <Image> clientImages = Arrays.asList(
            new Image("clientImage1", convertToBlob(loadImage("default-avatar.png")))
        );

        List <Image> eventImages = Arrays.asList(
            // adding .png to the name
            new Image("eventImage1.png", convertToBlob(loadImage("classes-diagram.png"))),
            new Image("eventImage2.png", convertToBlob(loadImage("database-diagram.png")))
        );

        allImages.addFirst(artistImages);   //We add the artistImages list to the big list
        allImages.add(1, clientImages);
        allImages.add(2, eventImages);

        return allImages;
    }

    public List<Artist> initializeArtists(List <Image> artistImages) {

        List<Artist> artists = Arrays.asList(
            new Artist("Duki", "Cantante argentino nacido en Buenos Aires el 24/06/1996. Sus temas abarcan sobre todo música urbana (trap, reggaeton)",artistImages.get(0), "duki", "DukiSSJ"),
            new Artist("Film Symphony Orchestra", "",artistImages.get(1), "", ""),
            new Artist("Aitana", "",artistImages.get(2), "", ""),
            new Artist("Juan Dávila", "",artistImages.get(3), "", ""),
            new Artist("Galder Varas", "",artistImages.get(4), "", ""),
            new Artist("Carlos Vives", "",artistImages.get(5), "", ""),
            new Artist("Vetusta Morla", "",artistImages.get(6), "", ""),
            new Artist("David Guetta", "",artistImages.get(7), "", ""),
            new Artist("Diana Krall", "",artistImages.get(8), "", ""),
            new Artist("Alejandro Sanz", "",artistImages.get(9), "", ""),
            new Artist("Niña Pastori", "",artistImages.get(10), "", ""),
            new Artist("Metallica", "",artistImages.get(11), "", "")
        );

        artistRepository.saveAll(artists);

        return artists;
    }

    public List<Event>  initializeEvents(List<Artist> artists, List<Image> eventImages) {

        /* This is for the initializeZones function. If u want to add zones to any event do it a set/list/array and pass it here
        To add anything (zones, sessions or discounts) u have to do events.get().getzones/getSessions/getDiscounts().add(zone/session/discount)

        Zone zonaVip = new Zone("Zona VIP", 500, new BigDecimal(74.99));
        zoneRepository.save(zonaVip);
        */

        List<Event> events = Arrays.asList(
            new Event("Concierto Duki Wizink Center", 20000, artists.get(0), null, null, "Wizink Center", "Música", eventImages, 3),
            new Event("Film Symphony Orchestra Wizink Center", 20000, artists.get(1), null, null, "Wizink Center", "Música", null,4),
            new Event("Concierto Aitana Wizink Center", 20000, artists.get(2), null, null, "Wizink Center", "Música", null,2),
            new Event("El show de Juan Dávila", 15000, artists.get(3), null, null, "Palacio Vistalegre", "Comedia", null,6),
            new Event("Riendo con Galder Varas", 15000, artists.get(4), null, null, "Palacio Vistalegre", "Comedia", null,2),
            new Event("Noche de Rock Urbano", 12000, artists.get(5), null, null, "WiZink Center", "Rock", null,3),
            new Event("Festival Indie Madrid", 18000, artists.get(6), null, null, "IFEMA", "Indie", null,3),
            new Event("Electro Night Experience", 22000, artists.get(7), null, null, "La Riviera", "Electrónica", null,4),
            new Event("Jazz & Soul Sessions", 8000, artists.get(8), null, null, "Teatro Real", "Jazz", null,4),
            new Event("Trap Revolution Tour", 16000, artists.get(0), null, null, "Palacio Vistalegre", "Trap", null,5),
            new Event("Clásicos del Pop Español", 14000, artists.get(9), null, null, "Movistar Arena", "Pop", null,1),
            new Event("Festival Flamenco Fusión", 9000, artists.get(10), null, null, "Teatro Circo Price", "Flamenco", null,2),
            new Event("Metal Legends Live", 20000, artists.get(11), null, null, "Auditorio Miguel Ríos", "Metal", null,3)
        );

        artists.get(0).getLastEvents().add(events.get(0));
        artists.get(0).getEventsIncoming().add(events.get(9));

        eventRepository.saveAll(events);
        artistRepository.saveAll(artists);

        return events; 
    }

    public Client initializeUsers(List <Image> clientImages) {

        Client defaultClient = new Client("pepe@gmail.com", "PepeG", passwordEncoder.encode("pepe123"), "Pepe", "Garcia", 33, 666666666, BigDecimal.ZERO, null, null, clientImages.get(0));
        Admin defaultAdmin = new Admin("adminEmail@gmail.com", "newAdmin", passwordEncoder.encode("admin"));

        userRepository.save(defaultClient);
        userRepository.save(defaultAdmin);

        return defaultClient;
    }

    public List<Discount> initializeDiscounts() {

        List<Discount> discounts = Arrays.asList(
            new Discount("WELCOME10", new BigDecimal("10.00"), true),
            new Discount("STUDENT5", new BigDecimal("5.00"), true),
            new Discount("VIP20", new BigDecimal("20.00"), true)
        );

        discountRepository.saveAll(discounts);
        return discounts;
    }

    public List<Zone> initializeZones() {

        List<Zone> zones = Arrays.asList(
            new Zone("General", 10000, new BigDecimal("45.00")),
            new Zone("VIP", 500, new BigDecimal("120.00")),
            new Zone("Front Stage", 800, new BigDecimal("80.00"))
        );

        zoneRepository.saveAll(zones);
        return zones;
    }

    public List<Session> initializeSessions(List<Event> events) {

        List<Session> sessions = Arrays.asList(
            new Session(events.get(0), null, Timestamp.valueOf("2026-06-10 21:00:00")),
            new Session(events.get(1), null, Timestamp.valueOf("2026-06-11 21:00:00")),
            new Session(events.get(2), null, Timestamp.valueOf("2026-06-12 21:00:00"))
        );

        sessionRepository.saveAll(sessions);
        return sessions;
    }

    public void initializePurchases(Client client, Session session, List <Zone> zones) {

            List<Ticket> tickets = Arrays.asList(
            new Ticket("QR1", zones.get(0), true),
            new Ticket("QR2", zones.get(0), true),
            new Ticket("QR3", zones.get(0), true),
            new Ticket("QR4", zones.get(0), true)
        );

        Purchase purchase = new Purchase(tickets, session, client);

        purchaseRepository.save(purchase);
    }

    /* This function will be executed after the database tables are created and will put the default data that will be at the website */
    @PostConstruct
    public void initializeDataBase() {
        List <List <Image> > images = initializeImages();

        Client defaultClient = initializeUsers(images.get(1));

        List <Artist> artists = initializeArtists(images.get(0));

        List<Event> events = initializeEvents(artists, images.get(2));

        initializeDiscounts();

        List<Zone> zones = initializeZones();

        List<Session> sessions = initializeSessions(events);

        initializePurchases(defaultClient, sessions.get(0), zones);
    }

}