package edu.ntnu.idatt2105.backend.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "listings")
public class Listing {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "brief_description", nullable = false, columnDefinition = "TEXT")
    private String briefDescription;
    @Column(name = "category", nullable = false)
    private String category;
    @Column(name = "location")
    private String address;
    @Column(name = "latitude", nullable = false)
    private double latitude;
    @Column(name = "longitude", nullable = false)
    private double longitude;
    @Column(name = "status", nullable = false)
    private Boolean status;
    @Column(name = "imageURL", nullable = false)
    private String imageURL;
}
