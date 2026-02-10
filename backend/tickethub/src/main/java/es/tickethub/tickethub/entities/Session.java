package es.tickethub.tickethub.entities;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Session {
    
    /* All the columns can't be null*/

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sessionID;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private Timestamp date;

    public Session() {
        /* The constructor for the database*/
    }

    // Constructor of the class
    public Session(Long sessionID, Event event, Timestamp date) {
        this.sessionID = sessionID;
        this.event = event;
        this.date = date;
    }
}
