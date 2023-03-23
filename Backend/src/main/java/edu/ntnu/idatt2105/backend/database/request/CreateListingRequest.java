package edu.ntnu.idatt2105.backend.database.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateListingRequest {

    private String description;
    private String briefDescription;
    private String category;
    private String address;
    private double latitude;
    private double longitude;
    private Boolean status;
    private String owner;
    private double price;

}
