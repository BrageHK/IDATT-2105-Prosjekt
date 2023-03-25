package edu.ntnu.idatt2105.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.DTO.ListingDTO;
import edu.ntnu.idatt2105.backend.DTO.UserDTO;
import edu.ntnu.idatt2105.backend.Repository.ListingRepository;
import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.Listing;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private ListingService listingService;

    public User getUserFromJTW(String authHeader) {
        String jwt = authHeader.substring(7);
        return userRepository.findByEmail(jwtService.extractUsername(jwt)).get();

    }

    public String userToJson(User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(userToDTO(user));
        return json;
    }

    public UserDTO userToDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }

    public String getFavoritesToJson(User user) {
        List<Listing> favourites = user.getFavourites();
        List<ListingDTO> listingDTOS = new ArrayList<>();
        for (Listing listing: favourites) {
            listingDTOS.add(listingService.convertToListingDTO(listing, true));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(listingDTOS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<String> addFavorite(Long listingId) {
        try {
            User user = userRepository.findById(jwtService.getAuthenticatedUserId()).get();
            List<Listing> favourites = user.getFavourites();
            Listing listing = listingRepository.findById(listingId).get();
            if (favourites.contains(listing)) {
                return ResponseEntity.badRequest().body("Listing already in favourites");
            }
            favourites.add(listing);
            user.setFavourites(favourites);
            userRepository.save(user);
            return ResponseEntity.ok("Listing added to favourites");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Listing couldn't be added to favourites: " + e.getMessage());
        }
    }

    /**
     * Removes a listing from the authenticated user's favourites.
     * Returns a bad request if the listing is not in the user's favourites.
     * Returns a bad request if the listing does not exist.
     * Returns a bad request if the user does not exist.
     * Returns ok if the listing was removed from the user's favourites.
     *
     * @param listingID the id of the listing to be removed from the user's favourites
     * @return a response entity with the appropriate status code and message
     */
    public ResponseEntity<String> removeFavorite(Long listingID) {
        try {
            User user = userRepository.findById(jwtService.getAuthenticatedUserId()).get();
            Listing listing = listingRepository.findById(listingID).get();
            List<Listing> favourites = user.getFavourites();
            if (!favourites.remove(listing)) {
                return ResponseEntity.badRequest().body("Listing not in favourites");
            }
            user.setFavourites(favourites);
            userRepository.save(user);
            return ResponseEntity.ok("Listing removed from favourites");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Listing couldn't be removed from favourites: " + e.getMessage());
        }
    }

    public String getListingsToJson(User user) {
        List<Listing> listings = user.getListings();
        List<ListingDTO> listingDTOS = new ArrayList<>();
        for (Listing listing: listings) {
            listingDTOS.add(listingService.convertToListingDTO(listing, user.getFavourites().contains(listing)));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(listingDTOS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if the authenticated user owns the listing with the given id. Returns true if the user owns the listing,
     * false otherwise.
     *
     * @param id the id of the listing to be checked
     * @return a response entity with the appropriate status code and message
     */
    public ResponseEntity<Boolean> isOwnedByUser(Long id) {
        if (jwtService.isAuthenticated()) {
            User user = userRepository.findById(jwtService.getAuthenticatedUserId()).get();
            Listing listing = listingRepository.findById(id).get();
            return ResponseEntity.ok(user.getListings().contains(listing));
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    public ResponseEntity<String> editUser(User user, UserDTO userDTO) {
        if(userDTO.getFirstName() != null)
            user.setFirstName(userDTO.getFirstName());
        if(userDTO.getLastName() != null)
            user.setLastName(userDTO.getLastName());
        if(userDTO.getPhoneNumber() != null)
            user.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getAddress() != null)
            user.setAddress(userDTO.getAddress());
        userRepository.save(user);
        return ResponseEntity.ok("User edited");
    }
}
