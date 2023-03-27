package edu.ntnu.idatt2105.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.DTO.ListingDTO;
import edu.ntnu.idatt2105.backend.DTO.UserDTO;
import edu.ntnu.idatt2105.backend.repository.ListingRepository;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.Listing;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.security.AuthenticationService;
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

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Returns a json string of the authenticated user. The user's password is not included in the json string.
     *
     * @param user the authenticated user
     * @return a json string of the authenticated user
     * @throws JsonProcessingException if the user cannot be converted to json
     */
    public String userToJson(User user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(userToDTO(user));
        return json;
    }

    /**
     * Returns a JSON string of every user in the database. The user's password is not included in the json string.
     *
     * @param users a list of all users in the database
     * @return a JSON string of every user in the database
     */
    public String usersToJson(List<User> users) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user: users) {
            userDTOS.add(userToDTO(user));
        }
        String json = objectMapper.writeValueAsString(userDTOS);
        return json;
    }

    /**
     * Converts a user to a userDTO. The userDTO does not contain the user's password.
     *
     * @param user the user to be converted
     * @return a userDTO
     */
    public UserDTO userToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }

    /**
     * Returns a json string of the authenticated user's favourite listings.
     *
     * @param user the authenticated user
     * @return a json string of the authenticated user's favourite listings
     */
    public String getFavoritesToJson(User user) {

        List<Listing> favourites = user.getFavourites();
        List<ListingDTO> listingDTOS = new ArrayList<>();
        for (Listing listing: favourites) {
            listingDTOS.add(listingService.convertToListingDTO(listing));
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
     * Adds a listing to the authenticated user's favourites. Returns a bad request if the listing is already in the
     * user's favourites. Returns a bad request if the listing does not exist. Returns a bad request if the user does
     * not exist. Returns ok if the listing was added to the user's favourites.
     *
     * @param listingId the id of the listing to be added to the user's favourites
     * @return a response entity with the appropriate status code and message
     */
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

    /**
     * Returns a list of the authenticated user's owned listings as a JSON string. Returns null if the user does not
     * exist. Returns null if the user has no owned listings.
     *
     * @param user the authenticated user
     * @return a JSON string containing the user's owned listings
     */
    public String getListingsToJson(User user) {
        if(user == null) {
            return null;
        }
        List<Listing> listings = user.getListings();
        List<ListingDTO> listingDTOS = new ArrayList<>();
        for (Listing listing: listings) {
            listingDTOS.add(listingService.convertToListingDTO(listing));
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

    /**
     * Edits the user with the given id. Returns a bad request if the user is not the authenticated user or an admin.
     *
     *
     * @param user the user to be edited
     * @param userDTO the userDTO containing the new information
     * @return a response entity with the appropriate status code and message
     */
    public ResponseEntity<String> editUser(User user, UserDTO userDTO) {
        if(!(authenticationService.isAdmin() || user.getId().equals(jwtService.getAuthenticatedUserId()))
                || userDTO.getEmail() != null)
            return ResponseEntity.badRequest().body("You are not allowed to edit this user");
        if(userDTO.getFirstName() != null)
            user.setFirstName(userDTO.getFirstName());
        if(userDTO.getLastName() != null)
            user.setLastName(userDTO.getLastName());
        if(userDTO.getPhoneNumber() != null)
            user.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getAddress() != null)
            user.setAddress(userDTO.getAddress());
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(jwtToken);
    }

    /**
     * Gets every user from the database and returns them as a JSON string. Does not return the password.
     *
     * @return a JSON string containing all users
     */
    public String getAllUsersToJson() throws JsonProcessingException {
        List<User> users = userRepository.findAll();
        return usersToJson(users);
    }
}
