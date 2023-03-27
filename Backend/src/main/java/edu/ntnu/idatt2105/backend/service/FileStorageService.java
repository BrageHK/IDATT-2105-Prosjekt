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

/**
 * Service for handling file uploads. The service will store the files in the directory src/main/resources/images.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final Path fileStorageLocation;
    Logger logger = org.slf4j.LoggerFactory.getLogger(FileStorageService.class);

    /**
     * Constructor for the FileStorageService. The file storage location is set to src/main/resources/images.
     */
    public FileStorageService() {
        this.fileStorageLocation = Paths.get("src/main/resources/images").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory for file storage.", e);
        }
    }

    /**
     * Store a file on the server. The file will be stored in a directory with the name of the listingID. The file
     * must have the correct extension and name. A file with the same name will be overwritten and might break the
     * listing. The filename has to be the same as the total number of files in the directory.
     *
     * @param file The file to store
     * @param id The id of the listing
     * @param filename The name of the file
     * @return The filename without the extension
     */
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

    /**
     * Get a single image from a listing. The image is returned as a byte array.
     *
     * @param listingId The id of the listing.
     * @param imageIndex The index of the image in the listing.
     * @return A ResponseEntity containing the image as a byte array.
     */
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

    /**
     * Deletes a folder and all its contents.
     *
     * @param folderName The name of the folder to delete.
     * @return True if the folder was deleted, false otherwise.
     * @throws IOException If the folder could not be deleted.
     */
    public boolean deleteFolder(String folderName) throws IOException {
        Path folderPath = this.fileStorageLocation.resolve(folderName).normalize();
        File folder = folderPath.toFile();
        if (!folder.exists()) {
            throw new RuntimeException("Folder not found: " + folderName);
        }
        FileUtils.deleteDirectory(folder);
        return true;
    }

    /**
     * Deletes a file from a directory. If the directory only contains one file, this method will throw an exception.
     * This is to prevent a listing from having no images.
     *
     * @param listingId The id of the listing that the file belongs to.
     * @param imageIndex The index of the image in the listing.
     * @return True if the file was deleted, false otherwise.
     */
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
