package edu.ntnu.idatt2105.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The request body for authenticating a listing. Also used for transferring user information.
 */
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
    private double price;
    private int numberOfPictures;
    // These two fields are to make frontend development easier.
    private Boolean isFavoriteToCurrentUser;
    private Boolean isCurrentUserOwner;

    private Long ownerId;
}