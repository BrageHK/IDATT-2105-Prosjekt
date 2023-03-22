package edu.ntnu.idatt2105.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
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

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid file name: " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file: " + fileName, e);
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
