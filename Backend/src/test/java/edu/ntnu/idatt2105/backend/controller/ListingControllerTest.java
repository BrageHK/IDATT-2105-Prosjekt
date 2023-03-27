package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.enums.Role;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.repository.ListingRepository;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.security.JWTService;
import edu.ntnu.idatt2105.backend.security.SecurityConfig;
import edu.ntnu.idatt2105.backend.service.ListingService;
import edu.ntnu.idatt2105.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.http.HttpHeaders;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ListingController.class)
@AutoConfigureMockMvc(addFilters = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ListingControllerTest {

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

    @MockBean
    private ListingService listingService;

    @MockBean
    private ListingController listingController;

    @MockBean
    private ListingRepository listingRepository;

    @WithMockUser(roles = "USER", username = "test", password = "password", authorities = "USER")
    @Test
    public void testGetAllListings() throws Exception {
        mockMvc.perform(get("/api/listing/7")
                        /*.header("authorization", "Bearer " + jwtService.generateToken(User.builder()
                                //.id(1L)
                                .firstName("John")
                                .lastName("Doe")
                                .email("test@test.no")
                                .password("password")
                                .role(Role.USER)
                                .address("Testveien 1")
                                .phoneNumber(12345678L)
                                .build()))*/)
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER", username = "test", password = "password", authorities = "USER")
    @Test
    public void testCreateListing() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "test image".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/listing/create")
                        .param("listing", "{\"description\": \"test\", \"briefDescription\": \"Luxury ocean views\", \"category\": 1, \"address\": \"1233456  St\", \"latitude\": 37.7749123, \"longitude\": -122.4194123, \"price\": 5021.00}")
                        .param("files", String.valueOf(file))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
