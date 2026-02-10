package es.tickethub.tickethub.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Admin extends User {

    public Admin(){
        // constructor JPA
    }

    public Admin(String email, String username, String password) {
        super(email, username, password, true); // admin = true
    }
}