package es.tickethub.tickethub.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Event {

    /* All the columns of the entity Event cannot be nullable except discounts*/
    
    /* For autogenerate the id we have to use GenerationType.IDENTITY instead GenerationType.AUTO*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventID;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del evento es necesario")
    //if you want to change the size... I do not mind
    @Size(max = 100, message = "El nombre del evento no puede tener más de 100 caracteres")
    private String name;

    @Column(nullable = false)
    @Min(value = 1, message = "No se puede hacer un evento sin asistentes")
    private Integer capacity;

    @Column(nullable = false)
    private Integer targetAge;
    /* This represents that the entity Artist is related with Event in a way that one Artist can have many Events associated to him*/
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    /* This represents that the entity Session is related with Event in a way that one Event can have many Sessions associated*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true, fetch = FetchType.LAZY)
    @Column(nullable = false) //I think this make no sense
    private List<Session> sessions = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private List<Zone> zones = new ArrayList<>();

    /* Here we don't have to put orphanRemoval because the discounts can be associated to more events*/
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")

    private List<Discount> discounts = new ArrayList<>();

    @Column(nullable = false)
    @NotBlank(message = "El lugar del evento es obligatorio")
    private String place;

    @Column(nullable = false)
    @NotBlank(message = "La categoría del evento es obligatoria")
    private String category;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)

    private List<Image> eventImages = new ArrayList<>();

    public Event() {
        /* The constructor for the database*/
    }

    // Constructor of the class (we have to put all the parameters that can not be null in the database)
    public Event(String name, Integer capacity, Artist artist,
        List<Session> sessions, List<Zone> zones, String place,
        String category, List<Image> eventImages, Integer targetAge) {

        this.name = name;
        this.capacity = capacity;
        this.targetAge = targetAge;
        this.artist = artist;
        if (sessions != null) {
            this.sessions = sessions;
        }
        if (zones != null) {
            this.zones = zones;
        }
        this.place = place;
        this.category = category;
        if (eventImages != null) {
            this.eventImages = eventImages;
        }
    }

    public Image getMainImage() {
        if (this.eventImages != null && !this.eventImages.isEmpty()) {
            return this.eventImages.get(0);
        }
        return null;
    }
}
