package es.tickethub.tickethub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String email;

    /* The username and password can be null because when a User is created for a purchase he only has the email*/
    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(nullable = false)
    private Boolean admin;

    @Version
    private Long version;

    /* Constructor for the Database*/
    public User() {
    }

    // Constructor of the class
    public User(String email, String username, String password, Boolean admin) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }
}
