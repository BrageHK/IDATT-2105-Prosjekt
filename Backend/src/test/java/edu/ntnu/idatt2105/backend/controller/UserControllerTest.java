package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.DTO.AuthenticationResponse;
import edu.ntnu.idatt2105.backend.DTO.PasswordEditRequest;
import edu.ntnu.idatt2105.backend.DTO.RegisterRequest;
import edu.ntnu.idatt2105.backend.DTO.UserDTO;
import edu.ntnu.idatt2105.backend.TestService;
import edu.ntnu.idatt2105.backend.enums.Role;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.security.JWTService;
import edu.ntnu.idatt2105.backend.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JWTService jwtService;

    private User user = User.builder()
            .firstName("Test")
            .lastName("Test")
            .email("test.test@test.test")
            .password("Test")
            .role(Role.USER)
            .address("Testveien 1")
            .phoneNumber(1881L)
            .build();
    private User admin = User.builder()
            .firstName("Admin")
            .lastName("Admin")
            .email("admin.admin@admin.admin")
            .password("Admin")
            .role(Role.ADMIN)
            .address("Adminveien 1")
            .phoneNumber(1881L)
            .build();

    private User user1;
    private User user2;
    private UserDTO userDTO;
    private List<User> users;
    private PasswordEditRequest passwordEditRequest;
    private AuthenticationResponse token1;
    private AuthenticationResponse token2;

    @Test
    public void getUserById() throws Exception {
        user1 = User.builder()
                //.id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test@test.no")
                .password("password")
                .role(Role.USER)
                .address("Testveien 1")
                .phoneNumber(12345678L)
                .build();
        when(jwtService.isAuthenticated()).thenReturn(true);
        when(jwtService.getAuthenticatedUserId()).thenReturn(user1.getId());
        when(userRepository.getReferenceById(user1.getId())).thenReturn(user1);
        when(userService.userToJson(user1)).thenReturn("User JSON");

        //var savedUser = userRepository.save(testService.getUser());
        //var jwtToken = jwtService.generateToken(testService.getUser());

        mockMvc.perform(
                get("/api/user/getUser")
                        .header("Authorization", "Bearer " + jwtService.generateToken(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User JSON"));
    }

    @Test
    public void getUserById_returns200() throws Exception {
        // Mock JWT authentication
        when(jwtService.isAuthenticated()).thenReturn(true);
        when(jwtService.getAuthenticatedUserId()).thenReturn(1L);

        // Mock user service to return a user JSON string
        String userJson = "{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\",\"phoneNumber\":12345678,\"address\":\"123 Main St\",\"role\":\"USER\"}";
        when(userService.userToJson(any())).thenReturn(userJson);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getUser"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(userJson));
    }

    @Test
    public void getUserById_notAuthenticated() throws Exception {
        //when(jwtService.isAuthenticated()).thenReturn(false);

        mockMvc.perform(get("/api/user/getUser"))
                .andExpect(status().isUnauthorized());
    }

}
