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
public class Artist {

    /* The artist columns can be nullable except the ID and the artistName*/
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long artistID;

    @Column(nullable = false)
    private String artistName;

    private String info;

    @OneToMany
    private List<Event> eventsIncoming;
    
    @OneToMany
    private List<Event> lastEvents;

    private Image artistImage;

    public Artist() {
        /* The constructor for the database*/
    }

    public Artist(Long artistID, String artistName) {
        this.artistID = artistID;
        this.artistName = artistName;
    }


}
