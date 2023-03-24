package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.security.JWTService;
import edu.ntnu.idatt2105.backend.service.ListingService;
import edu.ntnu.idatt2105.backend.service.UserService;
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
    @Autowired
    private final UserService userService;

    // create listing
    @PostMapping("/create")
    public ResponseEntity<?> createListing(
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

    @GetMapping("/{id}")
    public ResponseEntity<String> getListing(@PathVariable Long id) {
        return ResponseEntity.ok(listingService.getListingAsJson(id));
    }

    @GetMapping("/get20")
    public ResponseEntity<String> get20Listings() {
        return ResponseEntity.ok(listingService.get20ListingsAsJson());
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<?> deleteListing(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        // delete listing
        return ResponseEntity.ok(listingService.deleteListing(id, userService.getUserFromJTW(authHeader).getEmail()));
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<?> editListing(@PathVariable Long id) {
        // edit listing
        return ResponseEntity.ok("Listing edited");
    }

}
