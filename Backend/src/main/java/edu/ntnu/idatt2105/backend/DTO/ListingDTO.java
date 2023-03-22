package edu.ntnu.idatt2105.backend.DTO;

import edu.ntnu.idatt2105.backend.database.ListingImages;
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
public class ListingDTO {

    private Long id;
    private String description;
    private String briefDescription;
    private String category;
    private String address;
    private double latitude;
    private double longitude;
    private Boolean isSold;
    private String imageURL;
    private double price;

    private List<ListingImages> images = new ArrayList<>();
    private Long ownerId;
}