package es.tickethub.tickethub.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Artist {

    /* The artist columns can be nullable except the ID and the artistName*/
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistID;

    @Column(nullable = false)
    private String artistName;

    private String info;

    @OneToMany
    private List<Event> eventsIncoming = new ArrayList<>();
    
    @OneToMany
    private List<Event> lastEvents = new ArrayList<>();

    @OneToOne
    private Image artistImage;

    public Artist() {
        /* The constructor for the database*/
    }

    // Constructor of the class
    public Artist(String artistName) {
        this.artistName = artistName;
    }


}
