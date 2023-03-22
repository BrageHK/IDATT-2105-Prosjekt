package edu.ntnu.idatt2105.backend.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "brief_description", nullable = false, columnDefinition = "TEXT")
    private String briefDescription;
    @Column(name = "category", nullable = false)
    private String category;
    @Column(name = "address")
    private String address;
    @Column(name = "latitude", nullable = false)
    private double latitude;
    @Column(name = "longitude", nullable = false)
    private double longitude;
    @Column(name = "is_sold", nullable = false)
    private Boolean isSold;
    @Column(name = "imageURL", nullable = false)
    private String imageURL;
    @Column(name = "price", nullable = false)
    private double price;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListingImages> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
