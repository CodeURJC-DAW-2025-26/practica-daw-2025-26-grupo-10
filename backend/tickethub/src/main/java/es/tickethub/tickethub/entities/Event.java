package es.tickethub.tickethub.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(nullable = false)
    private Artist artist;

    @Column(nullable = false)
    @OneToMany
    private List<Session> sessions;

    @Column(nullable = false)
    @OneToMany
    private List<Zone> zones;

    @OneToMany
    private List<Discount> discounts;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    @OneToMany
    private List<Image> eventImages;

    public Event() {
        /* The constructor for the database*/
    }

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
