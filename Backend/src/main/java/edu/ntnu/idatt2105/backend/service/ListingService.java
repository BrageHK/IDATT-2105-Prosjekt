package edu.ntnu.idatt2105.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.DTO.ListingDTO;
import edu.ntnu.idatt2105.backend.repository.ListingRepository;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.filter.SearchRequest;
import edu.ntnu.idatt2105.backend.filter.SearchSpecification;
import edu.ntnu.idatt2105.backend.model.Listing;
import edu.ntnu.idatt2105.backend.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class ListingService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final JWTService jwtService;

    public Page<Listing> searchListing(SearchRequest request) {
        SearchSpecification<Listing> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return listingRepository.findAll(specification, pageable);
    }

    public boolean deleteListing(long id) throws IllegalArgumentException {
        AtomicBoolean deleted = new AtomicBoolean(false);
        listingRepository.findById(id).ifPresent(listing -> {
            if(listing.getOwner().getId().equals(jwtService.getAuthenticatedUserId())) {
                listingRepository.deleteById(id);
                try {
                    fileStorageService.deleteFolder(Long.toString(id));
                    deleted.set(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return deleted.get();
    }

    /**
     * Gets a listing from the database and returns it as a JSON string. If the user is logged in, the
     * listing is has a boolean value indicating whether the listing is a favourite of the user.
     *
     * @param id The id of the listing to be returned.
     * @return A JSON string containing the listing.
     */
    public ResponseEntity<String> getListingAsJson(long id) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        if(jwtService.isAuthenticated()) {
            try {
                json = mapper.writeValueAsString(
                        convertToListingDTO(listingRepository.findById(id).get())
                );
            } catch (Exception e) {
                logger.error("Error converting listing to json: " + e);
                return ResponseEntity.badRequest().body("Error converting listing to json: " + e);
            }
        } else {
            try {
                json = mapper.writeValueAsString(convertToListingDTO(listingRepository.findById(id).get()));
            } catch (Exception e) {
                logger.error("Error converting listing to json: " + e);
                return ResponseEntity.badRequest().body("Error converting listing to json: " + e);
            }
        }
        return ResponseEntity.ok(json);
    }

    /**
     * Gets 20 Listings. This method is deprecated.
     *
     * @return A JSON string containing 20 listings.
     */
    @Deprecated
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
                    .isCurrentUserOwner(false)
                    .isFavoriteToCurrentUser(false)
                    .numberOfPictures(files.size())
                    .dateCreated(java.time.LocalDateTime.now())
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

    public Page<Listing> addFavoriteBoolean(Page<Listing> listings) {

        for(Listing listing : listings) {
            listing.setIsFavoriteToCurrentUser(userRepository.findByEmail(jwtService.getAuthenticatedUserEmail())
                    .get().getFavourites().contains(listing));
        }
        return listings;
    }

    public ListingDTO convertToListingDTO(Listing listing) {
        boolean isFavoriteToCurrentUser = false;
        boolean isCurrentUserOwner = false;
        if(jwtService.isAuthenticated())
            isCurrentUserOwner = listing.getOwner().getEmail().equals(jwtService.getAuthenticatedUserEmail());
        if(jwtService.isAuthenticated())
            isFavoriteToCurrentUser = userRepository.findByEmail(jwtService.getAuthenticatedUserEmail())
                    .get().getFavourites().contains(listing);

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
                .isFavoriteToCurrentUser(isFavoriteToCurrentUser)
                .isCurrentUserOwner(isCurrentUserOwner)
                .build();
    }

    public ResponseEntity<String> editListing(Long id, ListingDTO listingDTO) {
        try {
            logger.info("ListingService: editListing: " + listingDTO);
            if(listingRepository.findById(id).get().getOwner().getId().equals(jwtService.getAuthenticatedUserId())) {
                Listing listing = listingRepository.findById(id).get();
                if(listingDTO.getDescription() != null)
                    listing.setDescription(listingDTO.getDescription());
                if(listingDTO.getBriefDescription() != null)
                    listing.setBriefDescription(listingDTO.getBriefDescription());
                if(listingDTO.getCategory() != null)
                    listing.setCategory(listingDTO.getCategory());
                if(listingDTO.getAddress() != null)
                    listing.setAddress(listingDTO.getAddress());
                if(listingDTO.getLatitude() != 0L)
                    listing.setLatitude(listingDTO.getLatitude());
                if(listingDTO.getLongitude() != 0L)
                    listing.setLongitude(listingDTO.getLongitude());
                listingRepository.save(listing);
                return ResponseEntity.ok("Listing edited");
            } else {
                return ResponseEntity.status(401).body("You are not the owner of this listing");
            }
        } catch(Exception e) {
            logger.error("Error editing listing: " + e);
            return ResponseEntity.status(400).body("Error editing listing");
        }
    }

    public ResponseEntity<String> addPictures(Long id, List<MultipartFile> files) {
        if(files.isEmpty()) {
            return ResponseEntity.status(400).body("No files selected");
        }
        // check if file is image
        for(MultipartFile file : files)
            if(!Objects.requireNonNull(file.getContentType()).contains("image"))
                return ResponseEntity.status(400).body("At least one file is not an image");
        try {
            if(listingRepository.findById(id).get().getOwner().getId().equals(jwtService.getAuthenticatedUserId())) {
                for(MultipartFile file : files) {
                    fileStorageService.handleFileUpload(
                            file,
                            id.toString(),
                            Integer.toString(listingRepository.findById(id).get().getNumberOfPictures())
                    );
                    Listing listing = listingRepository.findById(id).get();
                    listing.setNumberOfPictures(listing.getNumberOfPictures() + 1);
                    listingRepository.save(listing);
                }
                return ResponseEntity.ok("Pictures added");
            } else {
                return ResponseEntity.status(401).body("You are not the owner of this listing");
            }
        } catch (Exception e) {
            logger.error("Error adding pictures to listing: " + e);
            return ResponseEntity.status(400).body("Error adding pictures to listing: " + e);
        }
    }

    public ResponseEntity<String> removePicture(Long id, Long pictureId) {
        try {
            if(listingRepository.findById(id).get().getOwner().getId().equals(jwtService.getAuthenticatedUserId())) {
                fileStorageService.deleteFile(id.toString(), pictureId.toString());
                fileStorageService.removeFileGaps(id.toString());
                Listing listing = listingRepository.findById(id).get();
                listing.setNumberOfPictures(listing.getNumberOfPictures() - 1);
                listingRepository.save(listing);
                return ResponseEntity.ok("Picture removed");
            } else {
                return ResponseEntity.status(401).body("You are not the owner of this listing");
            }
        } catch (Exception e) {
            logger.error("Error removing picture from listing: " + e);
            return ResponseEntity.status(400).body("Error removing picture from listing: " + e);
        }
    }
}
