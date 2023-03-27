package edu.ntnu.idatt2105.backend.security;

import edu.ntnu.idatt2105.backend.DTO.AuthenticationRequest;
import edu.ntnu.idatt2105.backend.DTO.AuthenticationResponse;
import edu.ntnu.idatt2105.backend.DTO.RegisterRequest;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.logging.Logger;

/**
 * The service for authentication. This service is used to authenticate users and register new users.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    Logger logger = Logger.getLogger(AuthenticationService.class.getName());

    /**
     * This method is used to register a new user. It checks if the email is already taken. If the
     * email is already taken, an IllegalStateException is thrown. It also checks if the user is an admin. If the user is
     * not an admin, they can't register another admin. The user is created and saved to the database. A JWT token is
     * generated and returned.
     *
     * @param request The request containing the user information.
     * @return The response containing the JWT token.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken");
        }
        if(!isAdmin() && request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Only admins can create admins");
        }
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .address(request.getAddress())
                .phoneNumber(request.getPhonenumber())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * This method is used to authenticate a user with the authentication manager and spring security. It uses the
     * authentication request to get the email and password. The authentication manager authenticates the user. If the
     * user is not authenticated, an exception is thrown. The user is found in the database. If the user is not found,
     * an exception is thrown. A JWT token is generated and returned upon successful authentication.
     *
     * @param request The request containing the email and password.
     * @return The response containing the JWT token.
     * @throws NoSuchElementException If the user is not found in the database.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws NoSuchElementException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Updates the password of the authenticated user. The old password is checked against the password in the database.
     * If the old password is correct, the new password is encoded and saved in the database. If the old password is
     * wrong, a bad request is returned.
     *
     * @param oldPassword The old password.
     * @param newPassword The new password.
     * @return A response entity with the status code and message.
     */
    public ResponseEntity<String> updatePassword(String oldPassword, String newPassword) {
        if(oldPassword == null)
            return ResponseEntity.badRequest().body("Old password cannot be null");
        if(newPassword == null)
            return ResponseEntity.badRequest().body("New password cannot be null");

        if(userRepository.findById(jwtService.getAuthenticatedUserId()).isEmpty())
            return ResponseEntity.badRequest().body("User not found");

        User user = userRepository.findById(jwtService.getAuthenticatedUserId()).get();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is wrong");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        logger.info("Password changed for user " + user.getEmail() + " (id: " + user.getId() + ")");
        AuthenticationResponse response = authenticate(new AuthenticationRequest(user.getEmail(), newPassword));
        return ResponseEntity.ok(response.getToken());
    }

    /**
     * This method checks the SecurityContext to see if the user is an admin.
     *
     * @return True if the user is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains(
                Role.ADMIN.toString()
        );
    }
}

