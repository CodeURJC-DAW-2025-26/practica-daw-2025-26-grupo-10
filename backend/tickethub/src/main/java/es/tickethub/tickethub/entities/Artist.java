package es.tickethub.tickethub.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Artist {

    /* The artist columns can be nullable except the ID and the artistName */
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistID;

    @Column(nullable = false)
    @NotBlank(message="El nombre del artista es necesario")
    @Size(max = 100, message = "El nombre del artista no puede tener más de 100 caracteres")
    private String artistName;

    @Column(nullable = true)
    private String info;
    
    //Orphan removal -->   every child deleted in the collection of the father is deleted auto. in the DB
  
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> eventsIncoming = new ArrayList<>();
    
    @OneToMany
    private List<Event> lastEvents = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Image artistImage = new Image();

    private String instagram = "";

    private String twitter = "";

    public Artist() {
        /* The constructor for the database*/
    }

    // Constructor of the class
    public Artist(String artistName, String info, Image image, String instagram, String twitter) {
        this.artistName = artistName;
        this.info = info;
        if (image != null) {
            this.artistImage = image;
        }
        this.instagram = instagram;
        this.twitter = twitter;
    }


}
