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

    public String addFavorite(User user, Long listingId) {
        try {
            List<Listing> favourites = user.getFavourites();
            Listing listing = listingRepository.findById(listingId).get();
            if (favourites.contains(listing)) {
                return "Listing already in favourites";
            }
            favourites.add(listing);
            user.setFavourites(favourites);
            userRepository.save(user);
            return "Listing added to favourites";
        } catch (Exception e) {
            return "Listing couldn't be added to favourites: " + e.getMessage();
        }
    }

    public String removeFavorite(User user, Long listingID) {
        try {
            Listing listing = listingRepository.findById(listingID).get();
            List<Listing> favourites = user.getFavourites();
            if (!favourites.remove(listing)) {
                return "Listing not in favourites";
            }
            user.setFavourites(favourites);
            userRepository.save(user);
            return "Listing removed from favourites";
        } catch (Exception e) {
            return "Listing couldn't be removed from favourites: " + e.getMessage();
        }

    }

    public String getListingsToJson(User user) {
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
}
