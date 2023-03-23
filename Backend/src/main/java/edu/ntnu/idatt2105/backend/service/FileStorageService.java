package edu.ntnu.idatt2105.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("src/main/resources/images").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory for file storage.", e);
        }
    }

    public String handleFileUpload(MultipartFile file, String id, String filename) {
        String originalFileName = file.getOriginalFilename();
        String extension = "";

        // Extract the file extension from the original file name
        if (originalFileName != null && originalFileName.lastIndexOf(".") > 0) {
            extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }

        // Check if the file type is supported
        if (!extension.matches("(?i)(jpg|jpeg|png|gif|bmp)")) {
            throw new RuntimeException("File type not supported: " + extension);
        }

        // Construct the target location for the uploaded file
        Path targetLocation = Paths.get(fileStorageLocation + "/" + id + "/" + filename);

        try {
            // Create the target directory if it doesn't exist
            Files.createDirectories(targetLocation.getParent());

            // Copy the file contents to the target location
            Files.copy(file.getInputStream(), targetLocation);

            // Return the filename without the extension
            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + originalFileName, e);
        }
    }

    public File getFile(String fileName) {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + fileName);
        }
        return file;
    }

    public boolean deleteFile(String fileName) {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + fileName);
        }
        return file.delete();
    }
}
