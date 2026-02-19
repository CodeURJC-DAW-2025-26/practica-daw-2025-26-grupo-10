package es.tickethub.tickethub.entities;

import java.sql.Blob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageID;

    @Column(nullable = false)
    private String imageName;
    
    @Lob
    @Column(nullable = false)
    private Blob imageCode;     /* This type is a Binary Large Object for the SQL Database*/

    public Image() {
        /* The constructor for the database*/
    }

    // Constructor of the class
    public Image(String imageName, Blob imageCode) {
        this.imageName = imageName;
        this.imageCode = imageCode;
    }
}
