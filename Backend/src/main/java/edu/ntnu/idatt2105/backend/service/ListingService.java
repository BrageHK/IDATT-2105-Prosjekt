package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.database.Listing;
import edu.ntnu.idatt2105.backend.database.request.CreateListingRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListingService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    public void deleteListing(long id) {
        listingRepository.deleteById(id);
    }

    public boolean addListing(CreateListingRequest listingRequest) {
        try {
            Listing listing = Listing.builder()
                    .title(listingRequest.getTitle())
                    .description(listingRequest.getDescription())
                    .briefDescription(listingRequest.getBriefDescription())
                    .category(listingRequest.getCategory())
                    .latitude(listingRequest.getLatitude())
                    .longitude(listingRequest.getLongitude())
                    .price(listingRequest.getPrice())
                    .address(listingRequest.getAddress())
                    .imageURL(listingRequest.getImageURL())
                    .status(listingRequest.getStatus())
                    .owner(userRepository.findByEmail(listingRequest.getOwner()).get())
                    .build();
            Listing savedListing = listingRepository.save(listing);
            return true;
        } catch (Exception e) {
            logger.error("Error adding listing to database: " + e);
            return false;
        }

    }
}
