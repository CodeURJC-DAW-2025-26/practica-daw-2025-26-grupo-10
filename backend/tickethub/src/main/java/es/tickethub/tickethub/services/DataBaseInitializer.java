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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private byte[] loadImage(String imagePath) {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("static/images/" + imagePath)) {
            if (inputStream == null) {
                System.err.println("No se encontró la imagen: " + imagePath);
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
            new Image("imagen1", convertToBlob(loadImage("artists/duki.jpg"))),
            new Image("imagen2", convertToBlob(loadImage("artists/FSO.jpeg"))),
            new Image("imagen3", convertToBlob(loadImage("artists/aitana.jpg"))),
            new Image("imagen4", convertToBlob(loadImage("artists/juan_davila.jpeg"))),
            new Image("imagen5", convertToBlob(loadImage("artists/galder_varas.jpeg"))),
            new Image("imagen6", convertToBlob(loadImage("artists/linkin_park.jpg"))),
            new Image("imagen7", convertToBlob(loadImage("artists/vetusta_morla.jpg"))),
            new Image("imagen8", convertToBlob(loadImage("artists/David_Guetta.jpg"))),
            new Image("imagen9", convertToBlob(loadImage("artists/diana_krall.jpg"))),
            new Image("imagen10", convertToBlob(loadImage("artists/alejandro_sanz.jpg"))),
            new Image("imagen11", convertToBlob(loadImage("artists/niña_pastori.jpg"))),
            new Image("imagen12", convertToBlob(loadImage("artists/metallica.jpg")))
        );

        List <Image> clientImages = Arrays.asList(
            new Image("clientImage1", convertToBlob(loadImage("default-avatar.png"))),
            new Image("clientImage2", convertToBlob(loadImage("default-avatar.png")))

        );

        List <Image> eventImages = Arrays.asList(
            // adding .png to the name
            new Image("conciertoDukiImage1.jpg", convertToBlob(loadImage("events/concierto.jpg"))),
            new Image("conciertoDukiImage2.jpg", convertToBlob(loadImage("events/images.jpg"))),
            new Image("conciertoDukiImage3.png", convertToBlob(loadImage("events/concert_background.png"))),
            new Image("filmSymphonyImage1.jpg", convertToBlob(loadImage("events/FSO1.jpg"))),
            new Image("filmSymphonyImage2.jpg", convertToBlob(loadImage("events/FSO2.jpg"))),
            new Image("conciertoAitanaImage1.jpg", convertToBlob(loadImage("events/aitanaImage1.jpg"))),
            new Image("conciertoAitanaImage2.jpg", convertToBlob(loadImage("events/aitanaImage2.jpg"))),
            new Image("showJuanDavilaImage1.jpg", convertToBlob(loadImage("events/Juan_Davila_01.jpg"))),
            new Image("showJuanDavilaImage2.jpg", convertToBlob(loadImage("events/Juan_Davila_02.jpg"))),
            new Image("showGalderVarasImage1.jpg", convertToBlob(loadImage("events/galder_varas_1.jpg"))),
            new Image("showGalderVarasImage2.jpg", convertToBlob(loadImage("events/galder_varas_2.jpg"))),
            new Image("rockImage1.jpg", convertToBlob(loadImage("events/rock1.jpg"))),
            new Image("rockImage2.jpg", convertToBlob(loadImage("events/rock2.jpg"))),
            new Image("indieImage1.jpg", convertToBlob(loadImage("events/indie1.jpg"))),
            new Image("indieImage2.jpg", convertToBlob(loadImage("events/indie2.jpg"))),
            new Image("technoImage1.jpg", convertToBlob(loadImage("events/techno1.jpg"))),
            new Image("technoImage2.jpg", convertToBlob(loadImage("events/techno2.jpg"))),
            new Image("jazzImage1.jpg", convertToBlob(loadImage("events/jazz1.jpg"))),
            new Image("jazzImage2.jpg", convertToBlob(loadImage("events/jazz2.jpg"))),
            new Image("trapRevolutioneventImage1.jpg", convertToBlob(loadImage("events/concierto.jpg"))),
            new Image("trapRevolutioneventImage2.jpg", convertToBlob(loadImage("events/images.jpg"))),
            new Image("trapRevolutioneventImage3.png", convertToBlob(loadImage("events/concert_background.png"))),
            new Image("popEspañolImage1.jpg", convertToBlob(loadImage("events/pop_español1.jpg"))),
            new Image("popEspañolImage2.jpg", convertToBlob(loadImage("events/pop_español2.jpg"))),
            new Image("flamencoFusionImage1.jpg", convertToBlob(loadImage("events/flamenco_fusion1.jpg"))),
            new Image("flamencoFusionImage2.jpg", convertToBlob(loadImage("events/flamenco_fusion2.jpg"))),
            new Image("metalImage1.jpg", convertToBlob(loadImage("events/metal1.jpg"))),
            new Image("metalImage2.jpg", convertToBlob(loadImage("events/metal2.jpg")))
        );

        allImages.addFirst(artistImages);   //We add the artistImages list to the big list
        allImages.add(1, clientImages);
        allImages.add(2, eventImages);

        return allImages;
    }

    public List<Artist> initializeArtists(List <Image> artistImages) {
        if (artistRepository.count() > 0) {
            return artistRepository.findAll();
        }

        List<Artist> artists = Arrays.asList(
            new Artist("Duki", "Cantante argentino nacido en Buenos Aires el 24/06/1996. Sus temas abarcan sobre todo música urbana (trap, reggaeton)",artistImages.get(0), "duki", "DukiSSJ"),
            new Artist("Film Symphony Orchestra", "Orquesta sinfónica española especializada en interpretar bandas sonoras de cine en directo.", artistImages.get(1), "filmsymphony", "FilmSymphony"),
            new Artist("Aitana", "Cantante pop española que saltó a la fama en Operación Triunfo y es una de las artistas más influyentes del panorama actual.", artistImages.get(2), "aitana", "Aitanax"),
            new Artist("Juan Dávila", "Actor y cómico español conocido por sus espectáculos de improvisación y humor irreverente.", artistImages.get(3), "juandavila__", "JuanDavilaActor"),
            new Artist("Galder Varas", "Cómico y monologuista español destacado por su estilo cercano y su presencia en redes sociales.", artistImages.get(4), "galdervaras", "galdervaras"),
            new Artist("Linkin Park", "Banda estadounidense de rock alternativo y nu metal, famosa por fusionar rap, electrónica y guitarras contundentes.", artistImages.get(5), "linkinpark", "linkinpark"),
            new Artist("Vetusta Morla", "Banda española de indie rock conocida por sus letras profundas y su gran éxito en la escena alternativa.", artistImages.get(6), "vetustamorla", "vetustamorla"),
            new Artist("David Guetta", "DJ y productor francés, uno de los máximos exponentes de la música electrónica y dance a nivel mundial.", artistImages.get(7), "davidguetta", "davidguetta"),
            new Artist("Diana Krall", "Cantante y pianista canadiense de jazz, famosa por su voz suave y elegantes interpretaciones.", artistImages.get(8), "dianakrall", "DianaKrall"),
            new Artist("Alejandro Sanz", "Cantautor español de gran éxito internacional, conocido por sus baladas y fusión de pop y flamenco.", artistImages.get(9), "alejandrosanz", "AlejandroSanz"),
            new Artist("Niña Pastori", "Cantante española de flamenco y música latina, reconocida por su estilo moderno dentro del género.", artistImages.get(10), "npastorioficial", "npastorioficial"),
            new Artist("Metallica", "Banda estadounidense de heavy metal, considerada una de las más influyentes y exitosas de la historia.", artistImages.get(11), "metallica", "Metallica")
        );

        artistRepository.deleteAll();
        artistRepository.saveAll(artists);

        return artists;
    }

    public List<Event>  initializeEvents(List<Artist> artists, List<Image> eventImages, List<Zone> zones, List<Discount> discounts) {

        /* This is for the initializeZones function. If u want to add zones to any event do it a set/list/array and pass it here
        To add anything (zones, sessions or discounts) u have to do events.get().getzones/getSessions/getDiscounts().add(zone/session/discount)

        Zone zoneVip = new Zone("Zone VIP", 500, new BigDecimal(74.99));
        zoneRepository.save(zoneVip);
        */

        List<Event> events = Arrays.asList(
            new Event("Concierto Duki Wizink Center", artists.get(0), null, zones.subList(0,3), discounts, "Wizink Center", "Música", eventImages.subList(0, 3) , 3),
            new Event("Film Symphony Orchestra Wizink Center", artists.get(1), null, zones.subList(3,6), discounts, "Wizink Center", "Música", eventImages.subList(3, 5),4),
            new Event("Concierto Aitana Wizink Center", artists.get(2), null, zones.subList(6,9), discounts, "Wizink Center", "Música", eventImages.subList(5, 7),2),
            new Event("El show de Juan Dávila", artists.get(3), null, zones.subList(9,12), discounts, "Palacio Vistalegre", "Comedia", eventImages.subList(7, 9),6),
            new Event("Riendo con Galder Varas", artists.get(4), null, zones.subList(12,15), discounts, "Palacio Vistalegre", "Comedia", eventImages.subList(9, 11),2),
            new Event("Noche de Rock Urbano", artists.get(5), null, zones.subList(15,18), discounts, "WiZink Center", "Música", eventImages.subList(11, 13),3),
            new Event("Festival Indie Madrid", artists.get(6), null, zones.subList(18,21), discounts, "IFEMA", "Música", eventImages.subList(13, 15),3),
            new Event("Electro Night Experience", artists.get(7), null, zones.subList(21,24), discounts, "La Riviera", "Música", eventImages.subList(15, 17),4),
            new Event("Jazz & Soul Sessions", artists.get(8), null, zones.subList(24,27), discounts, "Teatro Real", "Música", eventImages.subList(17, 19),4),
            new Event("Trap Revolution T", artists.get(0), null, zones.subList(27,30), discounts, "Palacio Vistalegre", "Música", eventImages.subList(19, 22),5),
            new Event("Clásicos del Pop Español", artists.get(9), null, zones.subList(30,33), discounts, "Movistar Arena", "Música", eventImages.subList(22, 24),1),
            new Event("Festival Flamenco Fusión", artists.get(10), null, zones.subList(33,36), discounts, "Teatro Circo Price", "Música", eventImages.subList(24, 26),2),
            new Event("Metal Legends Live", artists.get(11), null, zones.subList(36,39), discounts, "Auditorio Miguel Ríos", "Música", eventImages.subList(26, 28),3)
        );

        for (Event event : events) {
            if (event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                event.getEventImages().get(0).setFirst(true);
                event.setMainImage(event.getEventImages().get(0));
            }
        }

        for (Discount discount : discounts) {
            discount.setEvents(events);
        }

        artists.get(0).getLastEvents().add(events.get(0));
        artists.get(0).getEventsIncoming().add(events.get(9));
        for (int i = 1; i < events.size(); i++) {
            if (i < 9) {
                artists.get(i).getEventsIncoming().add(events.get(i));
            } else if (i > 9) {
                artists.get(i - 1).getEventsIncoming().add(events.get(i));
            }
        }
        
        int j = 0;
        for (int i = 0; i < zones.size(); i++) {
            zones.get(i).setEvent(events.get(j));
            if (i % 3 == 0 && i != 0) {
                j++;
            }
        }

        for (Event event : events) {
            event.setCapacity(event.getZones().stream().mapToInt(Zone::getCapacity).sum());
        }

        eventRepository.saveAll(events);
        artistRepository.saveAll(artists);
        discountRepository.saveAll(discounts);

        return events; 
    }

    public Client initializeUsers(List <Image> clientImages) {
        Client defaultClient = new Client("pepe@gmail.com", "PepeG", passwordEncoder.encode("pepe123"), "Pepe", "Garcia", 33, 666666666, null, clientImages.get(0));
        Client defaultClient1 = new Client("manolo@gmail.com", "Manolin", passwordEncoder.encode("manolo123"), "Manolo", "Pérez", 19, 777777777, null, clientImages.get(1));

        Admin defaultAdmin = new Admin("adminEmail@gmail.com", "newAdmin", passwordEncoder.encode("admin"));

        userRepository.save(defaultClient);
        userRepository.save(defaultClient1);
        userRepository.save(defaultAdmin);

        return defaultClient;
    }

    public List<Discount> initializeDiscounts() {
        if (discountRepository.count() > 0) {
            return discountRepository.findAll();
        }

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
            new Zone("General", 12000, new BigDecimal("45.00")),
            new Zone("VIP", 600, new BigDecimal("120.00")),
            new Zone("Front Stage", 1500, new BigDecimal("80.00")),
            new Zone("General", 9500, new BigDecimal("30.00")),
            new Zone("VIP", 450, new BigDecimal("100.00")),
            new Zone("Front Stage", 1300, new BigDecimal("70.00")),
            new Zone("General", 8000, new BigDecimal("25.00")),
            new Zone("VIP", 400, new BigDecimal("80.00")),
            new Zone("Front Stage", 1000, new BigDecimal("50.00")),
            new Zone("General", 9000, new BigDecimal("35.50")),
            new Zone("VIP", 480, new BigDecimal("90.00")),
            new Zone("Front Stage", 1400, new BigDecimal("80.00")),
            new Zone("General", 11000, new BigDecimal("40.00")),
            new Zone("VIP", 520, new BigDecimal("85.00")),
            new Zone("Front Stage", 1200, new BigDecimal("60.00")),
            new Zone("General", 8500, new BigDecimal("27.99")),
            new Zone("VIP", 470, new BigDecimal("95.00")),
            new Zone("Front Stage", 1250, new BigDecimal("60.00")),
            new Zone("General", 10000, new BigDecimal("40.00")),
            new Zone("VIP", 550, new BigDecimal("115.00")),
            new Zone("Front Stage", 1500, new BigDecimal("84.99")),
            new Zone("General", 9200, new BigDecimal("35.00")),
            new Zone("VIP", 490, new BigDecimal("90.00")),
            new Zone("Front Stage", 1300, new BigDecimal("70.00")),
            new Zone("General", 8700, new BigDecimal("29.99")),
            new Zone("VIP", 420, new BigDecimal("79.99")),
            new Zone("Front Stage", 1100, new BigDecimal("59.99")),
            new Zone("General", 9300, new BigDecimal("34.00")),
            new Zone("VIP", 460, new BigDecimal("88.50")),
            new Zone("Front Stage", 1200, new BigDecimal("60.00")),
            new Zone("General", 12500, new BigDecimal("72.50")),
            new Zone("VIP", 600, new BigDecimal("120.00")),
            new Zone("Front Stage", 1600, new BigDecimal("94.99")),
            new Zone("General", 10500, new BigDecimal("50.00")),
            new Zone("VIP", 500, new BigDecimal("100.00")),
            new Zone("Front Stage", 1400, new BigDecimal("80.00")),
            new Zone("General", 11500, new BigDecimal("60.00")),
            new Zone("VIP", 650, new BigDecimal("130.00")),
            new Zone("Front Stage", 1700, new BigDecimal("95.00"))
        );

        return zones;
    }

    public List<Session> initializeSessions(List<Event> events) {

        List<Session> sessions = Arrays.asList(
            new Session(events.get(0), null, Timestamp.valueOf("2026-06-10 21:00:00")),
            new Session(events.get(0), null, Timestamp.valueOf("2026-06-11 20:30:00")),
            new Session(events.get(1), null, Timestamp.valueOf("2026-06-12 22:00:00")),
            new Session(events.get(1), null, Timestamp.valueOf("2026-06-13 19:30:00")),
            new Session(events.get(2), null, Timestamp.valueOf("2026-06-14 21:30:00")),
            new Session(events.get(2), null, Timestamp.valueOf("2026-06-15 20:00:00")),
            new Session(events.get(3), null, Timestamp.valueOf("2026-06-16 22:15:00")),
            new Session(events.get(3), null, Timestamp.valueOf("2026-06-17 19:45:00")),
            new Session(events.get(4), null, Timestamp.valueOf("2026-06-18 21:00:00")),
            new Session(events.get(4), null, Timestamp.valueOf("2026-06-19 20:30:00")),
            new Session(events.get(5), null, Timestamp.valueOf("2026-06-20 22:00:00")),
            new Session(events.get(5), null, Timestamp.valueOf("2026-06-21 19:30:00")),
            new Session(events.get(6), null, Timestamp.valueOf("2026-06-22 21:30:00")),
            new Session(events.get(6), null, Timestamp.valueOf("2026-06-23 20:00:00")),
            new Session(events.get(7), null, Timestamp.valueOf("2026-06-24 22:15:00")),
            new Session(events.get(7), null, Timestamp.valueOf("2026-06-25 19:45:00")),
            new Session(events.get(8), null, Timestamp.valueOf("2026-06-26 21:00:00")),
            new Session(events.get(8), null, Timestamp.valueOf("2026-06-27 20:30:00")),
            new Session(events.get(9), null, Timestamp.valueOf("2026-06-28 22:00:00")),
            new Session(events.get(9), null, Timestamp.valueOf("2026-06-29 19:30:00")),
            new Session(events.get(10), null, Timestamp.valueOf("2026-06-30 21:30:00")),
            new Session(events.get(10), null, Timestamp.valueOf("2026-07-01 20:00:00")),
            new Session(events.get(11), null, Timestamp.valueOf("2026-07-02 22:15:00")),
            new Session(events.get(11), null, Timestamp.valueOf("2026-07-03 19:45:00")),
            new Session(events.get(12), null, Timestamp.valueOf("2026-07-04 21:00:00")),
            new Session(events.get(12), null, Timestamp.valueOf("2026-07-05 20:30:00"))
        );

        sessionRepository.saveAll(sessions);
        return sessions;
    }

    public void initializePurchases(Client client, Session session, List<Zone> zones) {
        Purchase purchase = new Purchase();
        purchase.setClient(client);
        purchase.setSession(session);
        
        Ticket t1 = new Ticket();
        t1.setZone(zones.get(0));
        t1.setTicketPrice(zones.get(0).getPrice());
        t1.setPurchase(purchase); //
        t1.setIsActive(true);
        t1.setCode("QR-INIT-001");

        Ticket t2 = new Ticket();
        t2.setZone(zones.get(1));
        t2.setTicketPrice(zones.get(1).getPrice());
        t2.setPurchase(purchase);
        t2.setIsActive(true);
        t2.setCode("QR-INIT-002");

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(t1);
        tickets.add(t2);
        purchase.setTickets(tickets);

        BigDecimal total = t1.getTicketPrice().add(t2.getTicketPrice());
        purchase.setTotalPrice(total);

        purchaseRepository.save(purchase);
        }

    /* This function will be executed after the database tables are created and will put the default data that will be at the website */
    @PostConstruct
    public void initializeDataBase() {
        List <List <Image> > images = initializeImages();

        List<Zone> zones = initializeZones();

        Client defaultClient = initializeUsers(images.get(1));
 
        List <Artist> artists = initializeArtists(images.get(0));

        List<Discount> discounts = initializeDiscounts();

        List<Event> events = initializeEvents(artists, images.get(2), zones, discounts);

        List<Session> sessions = initializeSessions(events);

        initializePurchases(defaultClient, sessions.get(0), zones);
    }

}