package edu.ntnu.idatt2105.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.DTO.AuthenticationRequest;
import edu.ntnu.idatt2105.backend.DTO.AuthenticationResponse;
import edu.ntnu.idatt2105.backend.enums.Role;
import edu.ntnu.idatt2105.backend.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.DTO.RegisterRequest;
import edu.ntnu.idatt2105.backend.security.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    private AuthenticationRequest authenticationRequest;
    private RegisterRequest registerRequest;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    public void setUp() {
        authenticationRequest = new AuthenticationRequest("user@example.com", "password123");
        registerRequest = new RegisterRequest("John", "Doe", "exampl@gmail.com",
                "123456", "address", 12345678L, Role.USER);
        authenticationResponse = new AuthenticationResponse("jwt_token");
    }

    @Test
    void register() throws Exception {
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(
                new AuthenticationResponse("jwt_token")
        );

        RegisterRequest registerRequest = new RegisterRequest(
                "Ola", "Norman", "ola@example.com", "123456", "123 Main St",
                1881L, Role.USER
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String registerRequestJson = objectMapper.writeValueAsString(registerRequest);
        java.util.logging.Logger logger = Logger.getLogger(AuthControllerTest.class.getName());
        logger.info(registerRequestJson);

        mockMvc.perform(post("http://localhost:8080/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void authenticate() {
    }
}