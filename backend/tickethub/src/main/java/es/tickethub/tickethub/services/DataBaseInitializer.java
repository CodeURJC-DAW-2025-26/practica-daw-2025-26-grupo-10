package es.tickethub.tickethub.services;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.Admin;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.repositories.ArtistRepository;
import es.tickethub.tickethub.repositories.EventRepository;
import es.tickethub.tickethub.repositories.UserRepository;
import es.tickethub.tickethub.repositories.ZoneRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInitializer {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private UserRepository userRepository;

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
            new Image("clientImage1", convertToBlob(loadImage("database-diagram.png")))
        );

        allImages.addFirst(artistImages);   //We add the artistImages list to the big list
        allImages.add(1, clientImages);

        return allImages;
    }

    public List<Artist> initializeArtists(List <Image> artistImages) {

        List<Artist> artists = Arrays.asList(
            new Artist("Duki", "", artistImages.get(0), "", ""),
            new Artist("Film Symphony Orchestra", "", artistImages.get(1), "", ""),
            new Artist("Aitana", "", artistImages.get(2), "", ""),
            new Artist("Juan Dávila", "", artistImages.get(3), "", ""),
            new Artist("Galder Varas", "", artistImages.get(4), "", ""),
            new Artist("Carlos Vives", "", artistImages.get(5), "", ""),
            new Artist("Vetusta Morla", "", artistImages.get(6), "", ""),
            new Artist("David Guetta", "", artistImages.get(7), "", ""),
            new Artist("Diana Krall", "", artistImages.get(8), "", ""),
            new Artist("Alejandro Sanz", "", artistImages.get(9), "", ""),
            new Artist("Niña Pastori", "", artistImages.get(10), "", ""),
            new Artist("Metallica", "", artistImages.get(11), "", "")
        );

        artistRepository.saveAll(artists);

        return artists;
    }

    public void initializeEvents(List<Artist> artists) {

        /* This is for the initializeZones function. If u want to add zones to any event do it a set/list/array and pass it here
        To add anything (zones, sessions or discounts) u have to do events.get().getzones/getSessions/getDiscounts().add(zone/session/discount)

        Zone zonaVip = new Zone("Zona VIP", 500, new BigDecimal(74.99));
        zoneRepository.save(zonaVip);
        */

        List<Event> events = Arrays.asList(
            new Event("Concierto Duki Wizink Center", 20000, artists.get(0), null, null, "Wizink Center", "Música", null),
            new Event("Film Symphony Orchestra Wizink Center", 20000, artists.get(1), null, null, "Wizink Center", "Música", null),
            new Event("Concierto Aitana Wizink Center", 20000, artists.get(2), null, null, "Wizink Center", "Música", null),
            new Event("El show de Juan Dávila", 15000, artists.get(3), null, null, "Palacio Vistalegre", "Comedia", null),
            new Event("Riendo con Galder Varas", 15000, artists.get(4), null, null, "Palacio Vistalegre", "Comedia", null),
            new Event("Noche de Rock Urbano", 12000, artists.get(5), null, null, "WiZink Center", "Rock", null),
            new Event("Festival Indie Madrid", 18000, artists.get(6), null, null, "IFEMA", "Indie", null),
            new Event("Electro Night Experience", 22000, artists.get(7), null, null, "La Riviera", "Electrónica", null),
            new Event("Jazz & Soul Sessions", 8000, artists.get(8), null, null, "Teatro Real", "Jazz", null),
            new Event("Trap Revolution Tour", 16000, artists.get(0), null, null, "Palacio Vistalegre", "Trap", null),
            new Event("Clásicos del Pop Español", 14000, artists.get(9), null, null, "Movistar Arena", "Pop", null),
            new Event("Festival Flamenco Fusión", 9000, artists.get(10), null, null, "Teatro Circo Price", "Flamenco", null),
            new Event("Metal Legends Live", 20000, artists.get(11), null, null, "Auditorio Miguel Ríos", "Metal", null)
        );

        eventRepository.saveAll(events);
    }

    public void initializeUsers(List <Image> clientImages) {

        Client defaultClient = new Client("pepe@gmail.com", "PepeG", "pepe123", "Pepe", "Garcia", 33, 666666666, BigDecimal.ZERO, null, null, clientImages.get(0));
        Admin defaultAdmin = new Admin("adminEmail@gmail.com", "newAdmin", "admin");

        userRepository.save(defaultClient);
        userRepository.save(defaultAdmin);

    }

    /* This function will be executed after the database tables are created and will put the default data that will be at the website */
    @PostConstruct
    public void initializeDataBase() {

        List <List <Image> > images = initializeImages();

        initializeUsers(images.get(1));

        List <Artist> artists = initializeArtists(images.get(0));

        initializeEvents(artists);
    }

}
