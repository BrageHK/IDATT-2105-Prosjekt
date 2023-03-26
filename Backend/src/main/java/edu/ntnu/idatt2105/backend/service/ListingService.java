package edu.ntnu.idatt2105.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.DTO.ListingDTO;
import edu.ntnu.idatt2105.backend.model.Category;
import edu.ntnu.idatt2105.backend.repository.CategoryRepository;
import edu.ntnu.idatt2105.backend.repository.ListingRepository;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.filter.SearchRequest;
import edu.ntnu.idatt2105.backend.filter.SearchSpecification;
import edu.ntnu.idatt2105.backend.model.Listing;
import edu.ntnu.idatt2105.backend.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.security.JWTService;
import jakarta.persistence.EntityNotFoundException;
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

/**
 * Service class for listing related operations.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ListingService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;
    private final CategoryRepository categoryRepository;

    /**
     * Searches the database for listings that match the search parameters in the request. The search
     * parameters are specified in the request body. The search parameters are converted to a
     * specification and used to search the database. The search results are returned as a page.
     * The page number and size are specified in the request parameters.
     *
     * @param request The search parameters and page number and size.
     * @return A page containing the search results.
     */
    public Page<Listing> searchListing(SearchRequest request) {
        SearchSpecification<Listing> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return listingRepository.findAll(specification, pageable);
    }

    /**
     * Deletes a listing from the database. The listing is deleted if the user is the owner of the listing
     * or if the user is an admin.
     *
     * @param id The id of the listing to be deleted.
     * @return True if the listing was deleted, false otherwise.
     * @throws IllegalArgumentException If the listing does not exist.
     */
    public boolean deleteListing(long id) throws IllegalArgumentException {
        AtomicBoolean deleted = new AtomicBoolean(false);
        listingRepository.findById(id).ifPresent(listing -> {
            if(listing.getOwner().getId().equals(jwtService.getAuthenticatedUserId())
                    || authenticationService.isAdmin()) {
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

    /**
     * Adds a listing to the database. The listing is created from a JSON string and a list of images.
     * The images are stored in the file system of the backend.
     *
     * @param listingJson The JSON string containing the listing.
     * @param files A list of images.
     * @return The id of the listing.
     */
    public Long addListing(String listingJson, List<MultipartFile> files) {
        try {
            FileStorageService fileStorageService = new FileStorageService();

            ObjectMapper mapper = new ObjectMapper();
            ListingDTO listingDTO = mapper.readValue(listingJson, ListingDTO.class);

            Category category = categoryRepository.findById(listingDTO.getCategory()).get();
            logger.info("Ispresent: " + categoryRepository.findById(listingDTO.getCategory()).get());
            Listing listing = Listing.builder()
                    .description(listingDTO.getDescription())
                    .briefDescription(listingDTO.getBriefDescription())
                    .address(listingDTO.getAddress())
                    .latitude(listingDTO.getLatitude())
                    .longitude(listingDTO.getLongitude())
                    .price(listingDTO.getPrice())
                    .owner(userRepository.findByEmail(jwtService.getAuthenticatedUserEmail()).get())
                    .category(category)
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

    /**
     * Adds a boolean value to a listing indicating whether the listing is a favourite of the user. This is used
     * when returning a listing to the frontend. This way the frontend can know that the listing is a favorite, and
     * display the correct icon.
     *
     * @param listings The listings.
     * @return The listings with the boolean value added.
     */
    public Page<Listing> addFavoriteBoolean(Page<Listing> listings) {

        for(Listing listing : listings) {
            listing.setIsFavoriteToCurrentUser(userRepository.findByEmail(jwtService.getAuthenticatedUserEmail())
                    .get().getFavourites().contains(listing));
        }
        return listings;
    }

    /**
     * Converts a listing to a ListingDTO.
     *
     * @param listing The listing.
     * @return The ListingDTO.
     */
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
                .category(listing.getCategory().getId())
                .categoryName(listing.getCategory().getName())
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

    /**
     * Edits a listing. The listing is edited from a ListingDTO. The listing is found by id.
     *
     * @param id The id of the listing.
     * @param listingDTO The ListingDTO containing the new values.
     * @return A ResponseEntity containing a string.
     */
    public ResponseEntity<String> editListing(Long id, ListingDTO listingDTO) {
        try {
            logger.info("ListingService: editListing: " + listingDTO);
            if(listingRepository.findById(id).get().getOwner().getId().equals(jwtService.getAuthenticatedUserId())
            || authenticationService.isAdmin()) {
                Listing listing = listingRepository.findById(id).get();
                if(listingDTO.getPrice() > 0L)
                    listing.setPrice(listingDTO.getPrice());
                if(listingDTO.getDescription() != null)
                    listing.setDescription(listingDTO.getDescription());
                if(listingDTO.getBriefDescription() != null)
                    listing.setBriefDescription(listingDTO.getBriefDescription());
                if(listingDTO.getCategory() != null && categoryRepository.findById(listingDTO.getCategory()).isPresent())
                    listing.setCategory(categoryRepository.findById(listingDTO.getCategory()).get());
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

    /**
     * Adds images to a listing. The listing is found by id. Only the owner of the listing or an admin can add images.
     *
     * @param id The id of the listing.
     * @param files The images.
     * @return A ResponseEntity containing a string.
     */
    public ResponseEntity<String> addPictures(Long id, List<MultipartFile> files) {
        if(files.isEmpty()) {
            return ResponseEntity.status(400).body("No files selected");
        }
        // check if file is image
        for(MultipartFile file : files)
            if(!Objects.requireNonNull(file.getContentType()).contains("image"))
                return ResponseEntity.status(400).body("At least one file is not an image");
        try {
            if(listingRepository.findById(id).get().getOwner().getId().equals(jwtService.getAuthenticatedUserId())
            || authenticationService.isAdmin()) {
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

    /**
     * Removes a picture from a listing. The listing is found by id. Only the owner of the listing or an admin can
     * remove the picture. The picture is found by pictureId. The picture is removed from the database and the
     * backend file system.
     *
     * @param id The id of the listing.
     * @param pictureId The id of the picture.
     * @return A ResponseEntity containing a string.
     */
    public ResponseEntity<String> removePicture(Long id, Long pictureId) {
        try {
            if(listingRepository.findById(id).get().getOwner().getId().equals(jwtService.getAuthenticatedUserId())
            || authenticationService.isAdmin()) {
                if(!fileStorageService.deleteFile(id.toString(), pictureId.toString()))
                    return ResponseEntity.status(400).body("Error removing picture from listing");
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
