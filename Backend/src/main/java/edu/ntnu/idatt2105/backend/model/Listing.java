package edu.ntnu.idatt2105.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "listings")
public class Listing implements Serializable {

    @Serial
    private static final long serialVersionUID = -1730538653948604611L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "number_of_pictures", nullable = false)
    private int numberOfPictures;

    @JsonIgnore
    @ManyToMany(mappedBy = "favourites", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> favourites = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", 'description': '" + description + '\'' +
                ", 'briefDescription': '" + briefDescription + '\'' +
                ", 'category': '" + category + '\'' +
                ", 'address': '" + address + '\'' +
                ", 'latitude'': '" + latitude +
                ", 'longitude': '" + longitude +
                ", 'isSold': '" + isSold +
                ", 'price': '" + price +
                ", 'numberOfPictures': '" + numberOfPictures +
                ", 'favourites': '" + favourites +
                '}';
    }
}
