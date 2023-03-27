package edu.ntnu.idatt2105.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ntnu.idatt2105.backend.DTO.PasswordEditRequest;
import edu.ntnu.idatt2105.backend.DTO.UserDTO;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.security.JWTService;
import edu.ntnu.idatt2105.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user information. The user can get their information and edit their information.
 * The user can also get their favorite listings and owned listings.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AuthenticationService authenticationService;

    /**
     * Gets the logged-in user from JWT token.
     *
     * @return User as JSON string.
     * @throws JsonProcessingException if the user cannot be converted to JSON.
     */
    @Operation(summary = "Gets the logged-in user from JWT token", description = "The user must be logged in to get" +
            " their information")
    @GetMapping("/getUser")
    public ResponseEntity<String> getUserById() throws JsonProcessingException {
        if(!jwtService.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated, please log in");
        }
        User user = userRepository.getReferenceById(jwtService.getAuthenticatedUserId());
        return ResponseEntity.ok(userService.userToJson(user));
    }

    /**
     * Get seller with the given id. Only returns phone, email and name.
     *
     * @param id id of the user to get.
     * @return User info as JSON string.
     * @throws JsonProcessingException if the user cannot be converted to JSON.
     */
    @Operation(summary = "Get seller with the given id", description = "Only returns phone, email and name")
    @GetMapping("/getSeller/{id}")
    public ResponseEntity<String> getSellerById(@PathVariable Long id) throws JsonProcessingException {
        return userService.getSellerById(id);
    }

    /**
     * Get the favorite listings of the logged-in user.
     *
     * @return List of favorite listings as JSON string.
     */
    @Operation(summary = "Get the favorite listings of the logged-in user", description = "The user must be logged" +
            " in to get the favorite listings")
    @GetMapping("/getUser/favorites")
    public ResponseEntity<String> getUserFavourites() {
        if(!jwtService.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated, please log in");
        }
        User user = userRepository.getReferenceById(jwtService.getAuthenticatedUserId());
        return ResponseEntity.ok(userService.getFavoritesToJson(user));
    }

    /**
     * Deletes the user with the given id. The user must be logged in and have the correct permissions to delete a user.
     * The user can delete themselves, and an admin can delete any user.
     *
     * @param id id of the user to delete
     * @return 200 OK if the user was deleted, 401 if the user is not authenticated or does not have the correct
     * permissions.
     */
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * Get the owned listings of the logged-in user. The user must be logged in to get the owned listings.
     *
     * @return List of owned listings as JSON string.
     */
    @Operation(summary = "Get the owned listings of the logged-in user", description = "The user must be logged" +
            " in to get the owned listings")
    @GetMapping("/getUser/listings")
    public ResponseEntity<String> getUserListings() {
        if(!jwtService.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated, please log in");
        }
        User user = userRepository.getReferenceById(jwtService.getAuthenticatedUserId());
        return ResponseEntity.ok(userService.getListingsToJson(user));
    }

    /**
     * Check if a listing is owned by the user. Used to check if a user can edit a listing.
     *
     * @param id id of the listing
     * @return true if the listing is owned by the user, false otherwise
     */
    @Operation(summary = "Check if a listing is owned by the user", description = "Returns true if the listing is" +
            " owned by the user, false otherwise")
    @GetMapping("/getUser/isOwner/{id}")
    public ResponseEntity<Boolean> isOwnedByUser(@PathVariable Long id) {
        return userService.isOwnedByUser(id);
    }

    /**
     * Edits the logged-in user. Send a JSON string with the fields to be edited. The user must be logged in to edit
     * their information. With this endpoint, the user can edit all their information except their password.
     *
     * @param userDTO The user to be edited.
     * @return OK if the user was edited successfully, BAD_REQUEST if the user could not be edited.
     */
    @Operation(summary = "Edits the logged-in user", description = "The user must be logged in to edit their" +
            " information")
    @PutMapping("/editUser")
    public ResponseEntity<String> editUser(@RequestBody UserDTO userDTO) {
        if(!jwtService.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated, please log in");
        }
        User user = userRepository.getReferenceById(jwtService.getAuthenticatedUserId());
        return userService.editUser(user, userDTO);
    }

    /**
     * Edits the logged-in user's password. Send a JSON string with the fields to be edited. The user must be logged in to edit
     * their information. With this endpoint, the user can edit their password.
     *
     * @param passwordEditRequest The user to be edited. Send old and new password as JSON.
     * @return OK if the user was edited successfully, BAD_REQUEST if the user could not be edited.
     */
    @Operation(summary = "Edits the logged-in user's password", description = "The user must be logged in to edit" +
            " their password. They also need a old and a new password. The old password must be correct.")
    @PutMapping("/editUser/password")
    public ResponseEntity<String> editUserPassword(@RequestBody PasswordEditRequest passwordEditRequest) {
        if(!jwtService.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated, please log in");
        }
        return authenticationService.updatePassword(passwordEditRequest.getOldPassword(), passwordEditRequest.getNewPassword());
    }

    /**
     * Gets the admin status of the current user. Uses the JWT authentication token to check the user.
     *
     * @return True if user is admin, false otherwise.
     */
    @Operation(summary = "Gets the admin status of the current user", description = "Uses the JWT authentication" +
            " token to check the user. Returns true if user is admin, false otherwise.")
    @GetMapping("/getUser/isAdmin")
    public ResponseEntity<Boolean> isUserAdmin() {
        return ResponseEntity.ok(authenticationService.isAdmin());
    }

    /**
     * Gets every user in the database. Only admins can use this endpoint. Uses the JWT authentication token to check
     * the user.
     */
    @Operation(summary = "Gets every user in the database", description = "Only admins can use this endpoint. Uses" +
            " the JWT authentication token to check the user.")
    @GetMapping("/getAllUsers")
    public ResponseEntity<String> getAllUsers() throws JsonProcessingException {
        if(!jwtService.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated, please log in");
        }
        if(!authenticationService.isAdmin()) {
            return ResponseEntity.status(403).body("User is not admin");
        }
        return ResponseEntity.ok(userService.getAllUsersToJson());
    }



}