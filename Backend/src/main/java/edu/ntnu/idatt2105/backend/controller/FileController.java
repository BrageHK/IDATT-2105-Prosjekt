package edu.ntnu.idatt2105.backend.controller;

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

    /**
     * Get image from server by id.
     * @param id id of the listing the image belongs to.
     * @param id2 id of the image.
     * @return image as byte array.
     * @throws IOException if file not found.
     */
    @GetMapping("/{id}/{id2}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id, @PathVariable Long id2) throws IOException {
        String filePath = "src/main/resources/images/" + id + "/" + id2;

        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        // Read the contents of the file into a byte array
        byte[] fileContent = Files.readAllBytes(file.toPath());

        // Set the content type and content disposition headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData("attachment", Long.toString(id2));

        // Return the file content as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }
}