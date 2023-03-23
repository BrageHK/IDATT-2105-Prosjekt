package edu.ntnu.idatt2105.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.DTO.ListingDTO;
import edu.ntnu.idatt2105.backend.Repository.ListingImageRepository;
import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.Listing;
import edu.ntnu.idatt2105.backend.model.ListingImages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final ListingImageRepository listingImageRepository;

    public void deleteListing(long id) {
        listingRepository.deleteById(id);
    }

    public boolean addListing(String listingJson, List<MultipartFile> files, String email) {
        logger.info("ListingService: addListing");
        try {
            FileStorageService fileStorageService = new FileStorageService();

            ObjectMapper mapper = new ObjectMapper();
            ListingDTO listingDTO = mapper.readValue(listingJson, ListingDTO.class);
            ArrayList<ListingImages> images = new ArrayList<>();

            String fileName = fileStorageService.storeFile(files.get(0));
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("src/main/resources/images/")
                    .path(fileName)
                    .toUriString();
            listingDTO.setImageURL(fileDownloadUri);

            Listing listing = Listing.builder()
                    .description(listingDTO.getDescription())
                    .briefDescription(listingDTO.getBriefDescription())
                    .category(listingDTO.getCategory())
                    .address(listingDTO.getAddress())
                    .latitude(listingDTO.getLatitude())
                    .longitude(listingDTO.getLongitude())
                    .price(listingDTO.getPrice())
                    .imageURL(listingDTO.getImageURL())
                    .images(images)
                    .owner(userRepository.findByEmail(email).get())
                    .isSold(false)
                    .build();
            Listing savedListing = listingRepository.save(listing);

            for (int i = 1; i < files.size(); i++) {
                fileName = fileStorageService.storeFile(files.get(i));
                fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("src/main/resources/images/")
                        .path(fileName)
                        .toUriString();

                // First image in the request is the thumbnail in the listing
                ListingImages listingImage = ListingImages.builder()
                        .imageURL(fileDownloadUri)
                        .listing(listingRepository.getReferenceById(savedListing.getId()))
                        .build();
                images.add(listingImageRepository.save(listingImage));
            }
            return true;
        } catch (Exception e) {
            logger.error("Error adding listing to database: " + e);
            return false;
        }

    }
}
