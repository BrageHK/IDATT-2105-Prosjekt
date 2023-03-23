package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.security.JWTService;
import edu.ntnu.idatt2105.backend.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    // create listing
    @PostMapping("/create")
    public ResponseEntity<?> createListing(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("listing") String listingJson,
            @RequestHeader("Authorization") String authHeader
    ) {
        String jwt = authHeader.substring(7);
        String loggedInUserName = jwtService.extractUsername(jwt);

        String returnMessage;
        if(listingService.addListing(listingJson, files, loggedInUserName)) {
            returnMessage = "Listing created";
        } else {
            returnMessage = "Listing not created";
        }
        return ResponseEntity.ok(returnMessage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getListing(@PathVariable Long id) {
        return ResponseEntity.ok(listingService.getListingAsJson(id));
    }

    @GetMapping("/get20")
    public ResponseEntity<String> get20Listings() {
        return ResponseEntity.ok(listingService.get20ListingsAsJson());
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
