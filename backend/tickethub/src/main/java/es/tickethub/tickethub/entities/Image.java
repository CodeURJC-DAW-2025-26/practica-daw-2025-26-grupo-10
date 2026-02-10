package es.tickethub.tickethub.entities;

import java.sql.Blob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {
    
    @Id
    private String imageName;
    
    @Column(nullable = false)
    private Blob imageCode;     /* This type is a Binary Large Object for the SQL Database*/

    public Image() {
        /* The constructor for the database*/
    }

    public Image(String imageName, Blob imageCode) {
        this.imageName = imageName;
        this.imageCode = imageCode;
    }
}
