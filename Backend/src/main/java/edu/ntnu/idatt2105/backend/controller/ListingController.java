package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.security.JWTService;
import edu.ntnu.idatt2105.backend.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
@RequiredArgsConstructor
public class ListingController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ListingController.class);

    private final ListingRepository listingRepository;

    private final JWTService jwtService;

    private final ListingService ListingService;

    @PostMapping("/listing/{id}")
    public ResponseEntity<?> getListing(@PathVariable Long id) {
        // return info of listing as json
        return listingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // create listing
    @PostMapping("/listing/create")
    public ResponseEntity<?> createListing(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("listing") String listingJson,
            @RequestHeader("Authorization") String authHeader
    ) {

        String jwt = authHeader.substring(7);
        logger.info("hello from create listing: " + jwt);
        String loggedInUserName = jwtService.extractUsername(jwt);

        String returnMessage;
        if(ListingService.addListing(listingJson, files, loggedInUserName)) {
            returnMessage = "Listing created";
        } else {
            returnMessage = "Listing not created";
        }
        return ResponseEntity.ok(returnMessage);
    }

    @PostMapping("/listing/{id}/delete")
    public ResponseEntity<?> deleteListing(@PathVariable Long id) {
        // delete listing
        listingRepository.deleteById(id);
        return ResponseEntity.ok("Listing deleted");
    }

    @PostMapping("/listing/{id}/edit")
    public ResponseEntity<?> editListing(@PathVariable Long id) {
        // edit listing
        return ResponseEntity.ok("Listing edited");
    }

}
