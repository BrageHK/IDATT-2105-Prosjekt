package edu.ntnu.idatt2105.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.filter.SearchRequest;
import edu.ntnu.idatt2105.backend.model.Listing;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.request.EditRequest;
import edu.ntnu.idatt2105.backend.security.JWTService;
import edu.ntnu.idatt2105.backend.service.ListingService;
import edu.ntnu.idatt2105.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/listing")
@RequiredArgsConstructor
public class ListingController {

    @Autowired
    private final ListingRepository listingRepository;
    @Autowired
    private final JWTService jwtService;
    @Autowired
    private final ListingService listingService;
    @Autowired
    private final UserService userService;

    // create listing
    @PostMapping("/create")
    public ResponseEntity<String> createListing(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("listing") String listingJson,
            @RequestHeader("Authorization") String authHeader
    ) {
        String loggedInUserName = userService.getUserFromJTW(authHeader).getEmail();

        String returnMessage;
        Long num;
        if((num = listingService.addListing(listingJson, files, loggedInUserName)) != null) {
            returnMessage = num.toString();
        } else {
            returnMessage = "Listing not created";
        }
        return ResponseEntity.ok(returnMessage);
    }

    /**
     * Get listing by id as JSON. If the user is logged in, the favorite boolean is added to the listing.
     * User don't have to be logged in to get the listing.
     *
     * @param id ID if the listing
     * @param authHeader JWT token
     * @return Listing as JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getListing(
            @PathVariable Long id,
            @RequestHeader(name = "Authorization", required = false) String authHeader
    ) {
        if (authHeader != null)
            return ResponseEntity.ok(listingService.getListingAsJson(id, userService.getUserFromJTW(authHeader).getEmail()));
        else
            return ResponseEntity.ok(listingService.getListingAsJson(id));
    }

    // Deprecated, use search instead
    @GetMapping("/get20")
    public ResponseEntity<String> get20Listings() {
        return ResponseEntity.ok(listingService.get20ListingsAsJson());
    }

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Listing> search(
            @RequestBody SearchRequest request,
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        Page<Listing> page = listingService.searchListing(request);
        if(authHeader != null)
            page = listingService.addFavoriteBoolean(page, userService.getUserFromJTW(authHeader).getEmail());
        return page;
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<?> deleteListing(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        // delete listing
        return ResponseEntity.ok(listingService.deleteListing(id, userService.getUserFromJTW(authHeader).getEmail()));
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<String> editListing(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EditRequest editRequest
    ) throws JsonProcessingException {
        Logger logger = org.slf4j.LoggerFactory.getLogger(ListingController.class);
        logger.info("Listing: " + "listingJson");
        return ResponseEntity.ok(listingService.editListing(id, userService.getUserFromJTW(authHeader).getEmail(),
                "listingJson"));
    }

    @PostMapping("/{id}/edit/addPictures")
    public ResponseEntity<?> addPicture(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(listingService.addPictures(id, userService.getUserFromJTW(authHeader).getEmail(), files));
    }

    @GetMapping("/{id}/edit/removePicture/{pictureId}")
    public ResponseEntity<String> removePicture(
            @PathVariable Long id,
            @PathVariable Long pictureId,
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(listingService.removePicture(id, pictureId, userService.getUserFromJTW(authHeader).getEmail()));
    }

    @GetMapping("/{id}/addFavorite")
    public ResponseEntity<String> addFavorite(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id
    ) throws JsonProcessingException {
        // Extract user ID from the JWT token
        User user = userService.getUserFromJTW(authHeader);

        return ResponseEntity.ok(userService.addFavorite(user, id));
    }

    @GetMapping("/{id}/removeFavorite")
    public ResponseEntity<String> removeFavorite(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id
    ) throws JsonProcessingException {
        // Extract user ID from the JWT token
        User user = userService.getUserFromJTW(authHeader);

        return ResponseEntity.ok(userService.removeFavorite(user, id));
    }

}
