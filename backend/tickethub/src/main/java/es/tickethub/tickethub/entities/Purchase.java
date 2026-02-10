package es.tickethub.tickethub.entities;

import java.math.BigDecimal;
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
public class Purchase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseID;

    @Column(nullable = false)
    @OneToMany
    private List<Ticket> tickets;

    @Column(nullable = false)
    private Session session;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    public Purchase() {
        /* The constructor for the database*/
    }

    public Purchase(Long purchaseID, List<Ticket> tickets, Session session) {
        this.purchaseID = purchaseID;
        this.tickets = tickets;
        this.session = session;
        this.totalPrice = BigDecimal.ZERO;
        for (Ticket ticket : tickets) {
            this.totalPrice.add(ticket.getPrice());
        }
    }
}
