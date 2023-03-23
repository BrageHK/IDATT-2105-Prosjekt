package edu.ntnu.idatt2105.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.DTO.UserDTO;
import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/getUser")
    public ResponseEntity<String> getUserById(
            @RequestHeader("Authorization") String authHeader
    ) throws JsonProcessingException {
        // Extract user ID from the JWT token
        String jwt = authHeader.substring(7);
        User user = userRepository.findByEmail(jwtService.extractUsername(jwt)).get();


        Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

        // convert user to userDTO



        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(toDTO(user));
        logger.info("json: " + json);
        return ResponseEntity.ok(json);

    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }

}