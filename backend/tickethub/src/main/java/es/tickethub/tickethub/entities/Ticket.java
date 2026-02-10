package es.tickethub.tickethub.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Ticket {

    @Id
    private int ticketID;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private Session session;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean isActive;

    // Constructor vacío (JPA)
    public Ticket() {
    }

    // Constructor Completo
    public Ticket(int ticketID, String code, Session session, BigDecimal price, Boolean isActive) {
        this.ticketID = ticketID;
        this.code = code;
        this.session = session;
        this.price = price;
        this.isActive = isActive;
    }
    
}
