package edu.ntnu.idatt2105.backend.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final Path fileStorageLocation;
    Logger logger = org.slf4j.LoggerFactory.getLogger(FileStorageService.class);

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
            throw new RuntimeException("Failed to store file " + originalFileName + " New filename: " + filename, e);
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

    public ResponseEntity<byte[]> getImageInListing(String listingId, String imageIndex) {
        Path filePath = this.fileStorageLocation.resolve(listingId + "/" + imageIndex).normalize();
        File file = filePath.toFile();
        if (!file.exists()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getName()+"\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public boolean deleteFolder(String folderName) throws IOException {
        Path folderPath = this.fileStorageLocation.resolve(folderName).normalize();
        File folder = folderPath.toFile();
        if (!folder.exists()) {
            throw new RuntimeException("Folder not found: " + folderName);
        }
        FileUtils.deleteDirectory(folder);
        return true;
    }

    public boolean deleteFile(String listingId, String imageIndex) {
        Path filePath = this.fileStorageLocation.resolve(listingId + "/" + imageIndex).normalize();
        File file = filePath.toFile();
        if(Objects.requireNonNull(fileStorageLocation.resolve(listingId).toFile().listFiles()).length < 2) {
            throw new RuntimeException("Cannot delete last image in listing");
        }
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + listingId + "/" + imageIndex);
        }
        file.delete();
        return true;
    }

    /**
     * Renames all files in a directory to remove gaps in the numbering. For example, if the directory contains files
     * 0, 2, 3. This method will rename the files to 0, 1, 2. This method is useful when deleting files from a
     * directory, and you want to keep the numbering of the files in the directory.
     *
     * @param directoryPath The path to the directory that is to be sorted.
     */
    public void removeFileGaps(String directoryPath) {
        File directory = this.fileStorageLocation.resolve(directoryPath).toFile();
        File[] files = directory.listFiles();
        if (files == null) {
            throw new RuntimeException("Directory not found: " + directoryPath);
        }
        Arrays.sort(files);
        int expectedIndex = 0;
        for (File file : files) {
            String fileName = file.getName();
            int fileIndex = Integer.parseInt(fileName);
            if (fileIndex != expectedIndex) {
                String newFileName = Integer.toString(expectedIndex);
                File newFile = new File(fileStorageLocation.resolve(directoryPath) + File.separator + newFileName);
                if (file.renameTo(newFile)) {
                    logger.info("Renamed file " + fileName + " to " + newFileName);
                } else {
                    logger.info("Failed to rename file " + fileName);
                }
            }
            expectedIndex++;
        }
    }
}
