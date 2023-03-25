package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.DTO.AuthenticationRequest;
import edu.ntnu.idatt2105.backend.DTO.AuthenticationResponse;
import edu.ntnu.idatt2105.backend.security.authentication.AuthenticationService;
import edu.ntnu.idatt2105.backend.DTO.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private AuthenticationRequest authenticationRequest;
    private RegisterRequest registerRequest;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    public void setUp() {
        authenticationRequest = new AuthenticationRequest("user@example.com", "password123");
        registerRequest = new RegisterRequest("John", "Doe", "exampl@gmail.com",
                "password123", "address", 12345678L);
        authenticationResponse = new AuthenticationResponse("sample_token");
    }

    @Test
    void register() {
    }

    @Test
    void authenticate() {
    }
}