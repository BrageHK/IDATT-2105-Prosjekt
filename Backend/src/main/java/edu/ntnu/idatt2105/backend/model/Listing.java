package edu.ntnu.idatt2105.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The class that represents a listing in the database. A listing is an object that is for sale.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
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
    //@Column(name = "category", nullable = false)
    //private String category;
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
    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;
    @Column(name = "isFavoriteToCurrentUser")
    private Boolean isFavoriteToCurrentUser;
    @Column(name = "isCurrentUserOwner")
    private Boolean isCurrentUserOwner;

    @JsonIgnore
    @ManyToMany(mappedBy = "favourites", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> favourites = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, targetEntity = Category.class)
    @JoinColumn(name = "category_id", nullable = true, columnDefinition = "BIGINT DEFAULT 1")
    private Category category;


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
