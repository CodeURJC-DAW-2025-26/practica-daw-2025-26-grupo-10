package es.tickethub.tickethub.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Client extends User {

    @OneToMany(mappedBy = "client")//Fetchtype.LAZY by default in OneToMany
    private List<Purchase> purchases = new ArrayList<>();

    private String name;

    private String surname;

    @Min(0)
    private Integer age;

    private Integer phone;

    private BigDecimal coins;

    @ElementCollection
    private List<String> subjects = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Image profileImage;

    // Constructor for the Database
    public Client() {
    }

    // Constructor of the class
    public Client(
            String email,
            String username,
            String password,
            String name,
            String surname,
            Integer age,
            Integer phone,
            BigDecimal coins,
            List<String> subjects,
            List<Purchase> purchases,
            Image profileImage
    ) {
        super(email, username, password, false);
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.phone = phone;
        this.coins = coins;
        if (subjects != null) {
            this.subjects = subjects;
        }
        if (purchases != null) {
            this.purchases = purchases;
        }
        this.profileImage = profileImage;
    }
}