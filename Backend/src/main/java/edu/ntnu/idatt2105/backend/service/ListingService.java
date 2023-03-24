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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class ListingService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public String deleteListing(long id, String email) {
        AtomicReference<String> message = new AtomicReference<>("");
        listingRepository.findById(id).ifPresent(listing -> {
            if(listing.getOwner().getEmail().equals(email)) {
                listingRepository.deleteById(id);
                try {
                    logger.info("Is Images with id " + id + " deleted: "
                            + fileStorageService.deleteFolder(Long.toString(id)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                message.set("Listing deleted");
            } else {
                message.set("You are not the owner of this listing");
            }
        });
        return message.get();
    }

    public String getListingAsJson(long id) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(convertToListingDTO(listingRepository.findById(id).get()));
        } catch (Exception e) {
            logger.error("Error converting listing to json: " + e);
        }
        return json;
    }

    public String get20ListingsAsJson() {
        logger.info("Starting to convert listings to json");
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            List<Listing> listings = listingRepository.findAll();
            List<ListingDTO> listingDTOS = new ArrayList<>();
            logger.info("Loops starting");
            if(listings.size() > 20) {
                for (int i = 0; i < 20; i++) {
                    listingDTOS.add(convertToListingDTO(listings.get(i)));
                }
            } else {
                for (Listing listing : listings) {
                    listingDTOS.add(convertToListingDTO(listing));
                }
            }
            logger.info("ListingService: get20ListingsAsJson: " + listingDTOS.size() + " listings converted to json");
            json = mapper.writeValueAsString(listingDTOS);
        } catch (Exception e) {
            logger.error("Error converting listing to json: " + e);
        }
        return json;
    }

    public Long addListing(String listingJson, List<MultipartFile> files, String email) {
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
                    .numberOfPictures(files.size())
                    .build();

            Listing savedListing = listingRepository.save(listing);

            fileStorageService.handleFileUpload(files.get(0), savedListing.getId().toString(), "0");

            for (int i = 1; i < files.size(); i++) {
                fileStorageService.handleFileUpload(files.get(i), listing.getId().toString(), Integer.toString(i));
                logger.info("ListingService: addListing: image added");
            }

            return listing.getId();
        } catch (Exception e) {
            logger.error("Error adding listing to database: " + e);
            return null;
        }

    }

    public ListingDTO convertToListingDTO(Listing listing) {
        return ListingDTO.builder()
                .id(listing.getId())
                .description(listing.getDescription())
                .briefDescription(listing.getBriefDescription())
                .category(listing.getCategory())
                .address(listing.getAddress())
                .latitude(listing.getLatitude())
                .longitude(listing.getLongitude())
                .isSold(listing.getIsSold())
                .price(listing.getPrice())
                .numberOfPictures(listing.getNumberOfPictures())
                .ownerId(listing.getOwner().getId())
                .build();
    }
}
