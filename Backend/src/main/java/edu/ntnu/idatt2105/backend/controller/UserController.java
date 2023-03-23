package edu.ntnu.idatt2105.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.security.JWTService;
import edu.ntnu.idatt2105.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public ResponseEntity<String> getUserById(
            @RequestHeader("Authorization") String authHeader
    ) throws JsonProcessingException {
        // Extract user ID from the JWT token
        User user = userService.getUserFromJTW(authHeader);

        return ResponseEntity.ok(userService.userToJson(user));
    }

    @GetMapping("/getUser/favorites")
    public ResponseEntity<String> getUserFavourites(
            @RequestHeader("Authorization") String authHeader
    ) throws JsonProcessingException {
        // Extract user ID from the JWT token
        User user = userService.getUserFromJTW(authHeader);

        return ResponseEntity.ok(userService.getFavoritesToJson(user));
    }

    @PostMapping("/getUser/addFavorite")
    public ResponseEntity<String> addFavorite(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("listingId") Long listingId
    ) throws JsonProcessingException {
        // Extract user ID from the JWT token
        User user = userService.getUserFromJTW(authHeader);

        return ResponseEntity.ok(userService.addFavorite(user, listingId));
    }

    @PostMapping("/getUser/removeFavorite")
    public ResponseEntity<String> removeFavorite(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("listingId") Long listingId
    ) throws JsonProcessingException {
        // Extract user ID from the JWT token
        User user = userService.getUserFromJTW(authHeader);

        return ResponseEntity.ok(userService.removeFavorite(user, listingId));
    }
}