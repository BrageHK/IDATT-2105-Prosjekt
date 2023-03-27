package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.DTO.ListingDTO;
import edu.ntnu.idatt2105.backend.filter.SearchRequest;
import edu.ntnu.idatt2105.backend.model.Listing;
import edu.ntnu.idatt2105.backend.security.JWTService;
import edu.ntnu.idatt2105.backend.service.ListingService;
import edu.ntnu.idatt2105.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for listing endpoints.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/listing")
@RequiredArgsConstructor
public class ListingController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ListingController.class);

    @Autowired
    private final JWTService jwtService;
    @Autowired
    private final ListingService listingService;
    @Autowired
    private final UserService userService;

    /**
     * Create a new listing. The user must be logged in to create a listing. The listing is created
     * from a JSON string and a list of images. The images are saved to the server and are connected to the listing.
     *
     * @param files List of images.
     * @param listingJson JSON string of the listing.
     * @return Status code 201 if listing is created, 400 if listing is not created, 401 if not authenticated.
     */
    @Operation(summary = "Create a new listing", description = "The user must be logged in to create a listing." +
            " The listing is created from a JSON string and a list of images. The images are saved to the server" +
            " and are connected to the listing.")
    @PostMapping("/create")
    public ResponseEntity<String> createListing(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("listing") String listingJson // TODO: Change to DTO?
    ) {
        if(!jwtService.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated, please log in");
        }

        Long num;
        if((num = listingService.addListing(listingJson, files)) != null) {
            return ResponseEntity.ok(num.toString());
        } else {
            return ResponseEntity.badRequest().body("Listing not created");
        }
    }

    /**
     * Get listing by id as JSON. If the user is logged in, the favorite boolean is added to the listing.
     * User don't have to be logged in to get the listing.
     *
     * @param id ID if the listing.
     * @return Listing as JSON.
     */
    @Operation(summary = "Get a listing by id as JSON", description = "If the user is logged in, the favorite boolean "+
            "is added to the listing. User don't have to be logged in to get the listing.")
    @GetMapping("/{id}")
    public ResponseEntity<String> getListing(
            @PathVariable Long id
    ) {
        return listingService.getListingAsJson(id);
    }

    /**
     * Get 20 listings as JSON. The search method is strictly better than this method.
     * @return 20 listings as JSON.
     */
    @Deprecated
    @Operation(summary = "Get 20 listings as JSON", description = "The search method is strictly better than this method.")
    @GetMapping("/get20")
    public ResponseEntity<String> get20Listings() {
        return ResponseEntity.ok(listingService.get20ListingsAsJson());
    }

    /**
     * Search for listings. It is possible to search for every field in the listing. It is also possible to
     * filter for every field in the listing.
     *
     * @param request Request body with search and filter parameters.
     * @return Page of listings.
     */
    @Operation(summary = "Search for listings", description = "It is possible to search for every field in the " +
            "listing. It is also possible to filter for every field in the listing.")
    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Listing>> search(
            @RequestBody SearchRequest request) {
        try {
            Page<Listing> page = listingService.searchListing(request);
            if(jwtService.isAuthenticated())
                page = listingService.addFavoriteBoolean(page);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseEntity.status(400).build();
        }

    }

    /**
     * Deletes a listing. The user must be logged in to delete a listing.
     *
     * @param id ID of the listing.
     * @return A status message with either ok or error.
     */
    @Operation(summary = "Delete a listing", description = "The user must be logged in to delete a listing.")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteListing(@PathVariable Long id) {
        if(listingService.deleteListing(id))
            return ResponseEntity.ok("Listing deleted");
        else
            return ResponseEntity.status(401).body("Listing not deleted, please check your input data and that you" +
                    " are logged in");
    }

    /**
     * Edit a listing. The user must be logged in to edit a listing.
     *
     * @param id ID of the listing you want to edit.
     * @param listingDTO The new listing data.
     * @return A status message with either ok or error.
     */
    @Operation(summary = "Edit a listing", description = "The user must be logged in to edit a listing.")
    @PutMapping("/{id}/edit")
    public ResponseEntity<String> editListing(
            @PathVariable Long id,
            @RequestBody ListingDTO listingDTO
    ) {
        return listingService.editListing(id, listingDTO);
    }

    /**
     * Add pictures to a listing. The user must be logged in to add pictures to a listing.
     *
     * @param id ID of the listing.
     * @param files List of images.
     * @return A status message with either ok or error.
     */
    @Operation(summary = "Add pictures to a listing", description = "The user must be logged and own the listing " +
            "to add pictures to the listing.")
    @PutMapping("/{id}/edit/addPictures")
    public ResponseEntity<String> addPicture(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return listingService.addPictures(id, files);
    }

    /**
     * Remove a picture from a listing. The user must be logged in to remove a picture from a listing.
     * The user must be the owner of the listing to remove a picture.
     *
     * @param id ID of the listing.
     * @param pictureId ID of the picture.
     * @return
     */
    @Operation(summary = "Remove a picture from a listing", description = "The user must be logged in to remove a " +
            "picture from a listing. The user must be the owner of the listing to remove a picture.")
    @DeleteMapping("/{id}/edit/removePicture/{pictureId}")
    public ResponseEntity<String> removePicture(
            @PathVariable Long id,
            @PathVariable Long pictureId
    ) {
        return listingService.removePicture(id, pictureId);
    }

    /**
     * Add a listing to the favorites of the user. The user must be logged in to add a listing to the favorites.
     * The user can't add a listing to the favorites if the listing is already in the favorites.
     *
     * @param id ID of the listing.
     * @return A status message with either ok or error.
     */
    @Operation(summary = "Add a listing to the favorites of the user", description = "The user must be logged in to " +
            "add a listing to the favorites. The user can't add a listing to the favorites if the listing is already " +
            "in the favorites.")
    @PostMapping("/{id}/addFavorite")
    public ResponseEntity<String> addFavorite(
            @PathVariable Long id
    ) {
        return userService.addFavorite(id);
    }

    /**
     * Remove a listing from the favorites of the user. The user must be logged in to remove a listing from the
     * favorites. The user can't remove a listing from the favorites if the listing is not in the favorites.
     *
     * @param id ID of the listing.
     * @return A status message with either ok or error.
     */
    @Operation(summary = "Remove a listing from the favorites of the user", description = "The user must be logged in "+
            "to remove a listing from the favorites. The user can't remove a listing from the favorites if the " +
            "listing is not in the favorites.")
    @DeleteMapping("/{id}/removeFavorite")
    public ResponseEntity<String> removeFavorite(
            @PathVariable Long id
    ) {
        return userService.removeFavorite(id);
    }

    /**
     * Set a listing as sold. The user must be logged in to set a listing as sold. The user must be the owner of the
     * listing or be an admin to set a listing as sold.
     *
     * @param id ID of the listing.
     * @return A status message with either ok or error.
     */
    @Operation(summary = "Set a listing as sold", description = "The user must be logged in to set a listing as sold. "+
            "The user must be the owner of the listing or be an admin to set a listing as sold.")
    @PutMapping("/{id}/edit/setSold")
    public ResponseEntity<String> setSold(
            @PathVariable Long id
    ) {
        return listingService.setSold(id);
    }
}
