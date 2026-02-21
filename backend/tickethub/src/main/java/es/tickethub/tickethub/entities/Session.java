package es.tickethub.tickethub.entities;

import java.sql.Timestamp;
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Session {
    
    /* None of the columns can be null*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionID;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private List<Purchase> purchases = new ArrayList<>();

    @Column(nullable = false)
    private Timestamp date;

    public Session() {
        /* Constructor for the database*/
    }

    // Constructor of the class
    public Session(Event event, List<Purchase> purchases, Timestamp date) {
        this.event = event;
        this.date = date;
        if (purchases != null) {
            this.purchases = purchases;
        } else {
            this.purchases = new ArrayList<>();
        }
    }

}
