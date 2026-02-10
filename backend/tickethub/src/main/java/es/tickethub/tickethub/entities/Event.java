package es.tickethub.tickethub.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventID;

    
}
