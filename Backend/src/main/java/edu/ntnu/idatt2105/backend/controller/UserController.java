package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.database.User;
import edu.ntnu.idatt2105.backend.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class UserController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        // Extract user ID from the JWT token
        String jwt = authHeader.substring(7);
        String loggedInUserName = jwtService.extractUsername(jwt);

        // Check if the logged-in user's ID matches the requested user's ID
        if (loggedInUserName.equals(userRepository.findById(id).get().getEmail())) {
            logger.info("hello from get user by id: " + id);
            return userRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to access this user.");
        }
    }
}