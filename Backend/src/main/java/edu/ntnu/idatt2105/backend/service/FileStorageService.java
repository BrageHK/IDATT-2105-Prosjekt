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

    public String storeThumbnailImage(MultipartFile file, String fileName) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        if (!fileExtension.matches("(?i)(jpg|jpeg|png|gif|bmp)")) {
            throw new RuntimeException("File type not supported: " + fileExtension);
        }
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName + "." + fileExtension);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.getFileName().toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file: " + originalFileName, e);
        }
    }

    public String storeImage(MultipartFile file, String fileName, String folderName) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        if (!fileExtension.matches("(?i)(jpg|jpeg|png|gif|bmp)")) {
            throw new RuntimeException("File type not supported: " + fileExtension);
        }
        try {
            Path folderPath = this.fileStorageLocation.resolve(String.valueOf(folderName));
            if (!Files.exists(folderPath)) {
                Files.createDirectory(folderPath);
            }
            Path targetLocation = folderPath.resolve(fileName + "." + fileExtension);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.getFileName().toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file: " + originalFileName, e);
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
