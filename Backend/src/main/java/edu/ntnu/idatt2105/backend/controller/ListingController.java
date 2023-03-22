package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.database.request.CreateListingRequest;
import edu.ntnu.idatt2105.backend.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ListingController {

    @Autowired
    private ListingRepository listingRepository;

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
    public ResponseEntity<?> createListing(@RequestBody CreateListingRequest listingRequest) {
        String returnMessage;
        if(ListingService.addListing(listingRequest)) {
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
