package es.tickethub.tickethub.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Event {

    /* All the columns of the entity Event cannot be nullable except discounts*/
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    /* This represents that the entity Artist is related with Event in a way that one Artist can have many Events associated to him*/
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    /* This represents that the entity Session is related with Event in a way that one Event can have many Sessions associated*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    private List<Session> sessions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<Zone> zones;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<Discount> discounts;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id", nullable = false)
    private List<Image> eventImages;

    public Event() {
        /* The constructor for the database*/
    }

    // Constructor of the class
    public Event(Long eventID, String name, Integer capacity, Artist artist,
        List<Session> sessions, List<Zone> zones, List<Discount> discounts, String place, String category, List<Image> eventImages) {

        this.eventID = eventID;
        this.name = name;
        this.capacity = capacity;
        this.artist = artist;
        this.sessions = sessions;
        this.zones = zones;
        this.discounts = discounts;
        this.place = place;
        this.category = category;
        this.eventImages = eventImages;
    }
}
