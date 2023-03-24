package edu.ntnu.idatt2105.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.DTO.ListingDTO;
import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.filter.SearchRequest;
import edu.ntnu.idatt2105.backend.filter.SearchSpecification;
import edu.ntnu.idatt2105.backend.model.Listing;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class ListingService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public Page<Listing> searchListing(SearchRequest request) {
        SearchSpecification<Listing> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        return listingRepository.findAll(specification, pageable);
    }

    public String deleteListing(long id, String email) {
        AtomicReference<String> message = new AtomicReference<>("");
        listingRepository.findById(id).ifPresent(listing -> {
            if(listing.getOwner().getEmail().equals(email)) {
                listingRepository.deleteById(id);
                try {
                    logger.info("Is Images with id " + id + " deleted: "
                            + fileStorageService.deleteFolder(Long.toString(id)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                message.set("Listing deleted");
            } else {
                message.set("You are not the owner of this listing");
            }
        });
        return message.get();
    }

    public String getListingAsJson(long id) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(convertToListingDTO(listingRepository.findById(id).get(), false));
        } catch (Exception e) {
            logger.error("Error converting listing to json: " + e);
        }
        return json;
    }

    public String getListingAsJson(long id, String email) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(convertToListingDTO(listingRepository.findById(id).get(),
                    userRepository.findByEmail(email).get().getFavourites().contains(listingRepository.findById(id).get())));
        } catch (Exception e) {
            logger.error("Error converting listing to json: " + e);
        }
        return json;
    }

    public String get20ListingsAsJson() {
        logger.info("Starting to convert listings to json");
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            List<Listing> listings = listingRepository.findAll();
            List<ListingDTO> listingDTOS = new ArrayList<>();
            logger.info("Loops starting");
            if(listings.size() > 20) {
                for (int i = 0; i < 20; i++) {
                    listingDTOS.add(convertToListingDTO(listings.get(i)));
                }
            } else {
                for (Listing listing : listings) {
                    listingDTOS.add(convertToListingDTO(listing));
                }
            }
            logger.info("ListingService: get20ListingsAsJson: " + listingDTOS.size() + " listings converted to json");
            json = mapper.writeValueAsString(listingDTOS);
        } catch (Exception e) {
            logger.error("Error converting listing to json: " + e);
        }
        return json;
    }

    public Long addListing(String listingJson, List<MultipartFile> files, String email) {
        try {
            FileStorageService fileStorageService = new FileStorageService();

            ObjectMapper mapper = new ObjectMapper();
            ListingDTO listingDTO = mapper.readValue(listingJson, ListingDTO.class);

            Listing listing = Listing.builder()
                    .description(listingDTO.getDescription())
                    .briefDescription(listingDTO.getBriefDescription())
                    .category(listingDTO.getCategory())
                    .address(listingDTO.getAddress())
                    .latitude(listingDTO.getLatitude())
                    .longitude(listingDTO.getLongitude())
                    .price(listingDTO.getPrice())
                    .owner(userRepository.findByEmail(email).get())
                    .isSold(false)
                    .isFavoriteToCurrentUser(false)
                    .numberOfPictures(files.size())
                    .dateCreated(java.time.LocalDateTime.now())
                    .build();

            Listing savedListing = listingRepository.save(listing);

            fileStorageService.handleFileUpload(files.get(0), savedListing.getId().toString(), "0");

            for (int i = 1; i < files.size(); i++) {
                fileStorageService.handleFileUpload(files.get(i), listing.getId().toString(), Integer.toString(i));
                logger.info("ListingService: addListing: image added");
            }

            return listing.getId();
        } catch (Exception e) {
            logger.error("Error adding listing to database: " + e);
            return null;
        }
    }

    public Page<Listing> addFavoriteBoolean(Page<Listing> listings, String email) {
        for(Listing listing : listings) {
            listing.setIsFavoriteToCurrentUser(userRepository.findByEmail(email).get().getFavourites().contains(listing));
        }
        return listings;
    }

    public ListingDTO convertToListingDTO(Listing listing, Boolean isFavoriteToCurrentUser) {
        return ListingDTO.builder()
                .id(listing.getId())
                .description(listing.getDescription())
                .briefDescription(listing.getBriefDescription())
                .category(listing.getCategory())
                .address(listing.getAddress())
                .latitude(listing.getLatitude())
                .longitude(listing.getLongitude())
                .isSold(listing.getIsSold())
                .price(listing.getPrice())
                .numberOfPictures(listing.getNumberOfPictures())
                .ownerId(listing.getOwner().getId())
                .isFavoriteToCurrentUser(isFavoriteToCurrentUser)
                .build();
    }

    public ListingDTO convertToListingDTO(Listing listing) {
        return ListingDTO.builder()
                .id(listing.getId())
                .description(listing.getDescription())
                .briefDescription(listing.getBriefDescription())
                .category(listing.getCategory())
                .address(listing.getAddress())
                .latitude(listing.getLatitude())
                .longitude(listing.getLongitude())
                .isSold(listing.getIsSold())
                .price(listing.getPrice())
                .numberOfPictures(listing.getNumberOfPictures())
                .ownerId(listing.getOwner().getId())
                .isFavoriteToCurrentUser(false)
                .build();
    }

    public String editListing(Long id, String email, String listingJson) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            logger.info("ListingService: editListing: " + listingJson);
            ListingDTO listingDTO = mapper.readValue(listingJson, ListingDTO.class);
            logger.info("ListingService: editListing: " + listingDTO.toString());
            if(listingRepository.findById(id).get().getOwner().getEmail().equals(email)) {
                Listing listing = listingRepository.findById(id).get();
                if(listingDTO.getDescription() != null)
                    listing.setDescription(listingDTO.getDescription());
                if(listingDTO.getBriefDescription() != null)
                    listing.setBriefDescription(listingDTO.getBriefDescription());
                if(listingDTO.getCategory() != null)
                    listing.setCategory(listingDTO.getCategory());
                if(listingDTO.getAddress() != null)
                    listing.setAddress(listingDTO.getAddress());
                if(listingDTO.getLatitude() == 0L)
                    listing.setLatitude(listingDTO.getLatitude());
                if(listingDTO.getLongitude() == 0L)
                    listing.setLongitude(listingDTO.getLongitude());
                listingRepository.save(listing);
                return "Listing edited";
            } else {
                return "You are not the owner of this listing";
            }
        } catch(Exception e) {
            logger.error("Error editing listing: " + e);
            return "Error editing listing";
        }
    }

    public String addPictures(Long id, String email, List<MultipartFile> files) {
        if(files.isEmpty()) {
            return "No files selected";
        }
        // check if file is image
        for(MultipartFile file : files)
            if(!Objects.requireNonNull(file.getContentType()).contains("image"))
                return "File is not an image";

        try {
            if(listingRepository.findById(id).get().getOwner().getEmail().equals(email)) {
                for(MultipartFile file : files) {
                    fileStorageService.handleFileUpload(file, id.toString(), Integer.toString(listingRepository.findById(id).get().getNumberOfPictures()));
                    Listing listing = listingRepository.findById(id).get();
                    listing.setNumberOfPictures(listing.getNumberOfPictures() + 1);
                    listingRepository.save(listing);
                }
                return "Pictures added";
            } else {
                return "You are not the owner of this listing";
            }
        } catch (Exception e) {
            logger.error("Error adding pictures to listing: " + e);
            return "Error adding pictures to listing";
        }

    }

    public String removePicture(Long id, Long pictureId, String email) {
        try {
            if(listingRepository.findById(id).get().getOwner().getEmail().equals(email)) {
                fileStorageService.deleteFile(id.toString(), pictureId.toString());
                fileStorageService.removeFileGaps(id.toString());
                Listing listing = listingRepository.findById(id).get();
                listing.setNumberOfPictures(listing.getNumberOfPictures() - 1);
                listingRepository.save(listing);
                return "Picture removed";
            } else {
                return "You are not the owner of this listing";
            }
        } catch (Exception e) {
            logger.error("Error removing picture from listing: " + e);
            return "Error removing picture from listing";
        }

    }
}
