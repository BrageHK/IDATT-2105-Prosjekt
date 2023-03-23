package edu.ntnu.idatt2105.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.DTO.ListingDTO;
import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.Listing;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    public void deleteListing(long id) {
        listingRepository.deleteById(id);
    }

    public boolean addListing(String listingJson, List<MultipartFile> files, String email) {
        logger.info("ListingService: addListing");
        try {
            FileStorageService fileStorageService = new FileStorageService();

            ObjectMapper mapper = new ObjectMapper();
            ListingDTO listingDTO = mapper.readValue(listingJson, ListingDTO.class);

            Listing listing = Listing.builder()
                    .description(listingDTO.getDescription())
                    .briefDescription(listingDTO.getBriefDescription())
                    .category(listingDTO.getCategory())
                    .address(listingDTO.getAddress())
                    .latitude(listingDTO.getLatitude())
                    .longitude(listingDTO.getLongitude())
                    .price(listingDTO.getPrice())
                    .owner(userRepository.findByEmail(email).get())
                    .isSold(false)
                    .build();
            Listing savedListing = listingRepository.save(listing);

            fileStorageService.storeThumbnailImage(files.get(0), savedListing.getId().toString());

            for (int i = 1; i < files.size(); i++) {
                fileStorageService.storeImage(files.get(i), Integer.toString(i), listing.getId().toString());
            }

            return true;
        } catch (Exception e) {
            logger.error("Error adding listing to database: " + e);
            return false;
        }

    }
}
