package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;
    /**
     * Get image from server by listing ID and image ID.
     *
     * @param listingId id of the listing the image belongs to
     * @param imageId id of the image. ID starts with 0
     * @return image as byte array or error message
     */
    @Operation(summary = "Get image from server by id", description = "Get image from server by listing ID and image ID.")
    @GetMapping("/{listingId}/{imageId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long listingId, @PathVariable Long imageId) {
       return fileStorageService.getImageInListing(listingId.toString(), imageId.toString());
    }
}