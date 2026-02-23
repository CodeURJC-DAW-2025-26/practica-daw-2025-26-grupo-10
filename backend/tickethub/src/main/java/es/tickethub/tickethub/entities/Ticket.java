package es.tickethub.tickethub.entities;

import java.math.BigDecimal;

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

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketID;

    /* This code will be to generate an unique QR code for the ticket to be scanned*/
    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    private BigDecimal ticketPrice;

    @Column(nullable = false)
    private Boolean isActive;

    /* Constructor for the Database*/
    public Ticket() {
    }

    // Constructor of the class
    public Ticket(String code, Zone zone, Boolean isActive) {
        this.code = code;
        //this.session = session;
        this.zone = zone;
        this.isActive = isActive;
        this.ticketPrice = zone.getPrice();
    }
    
}
