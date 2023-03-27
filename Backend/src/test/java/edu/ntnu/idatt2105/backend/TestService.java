package edu.ntnu.idatt2105.backend;

import edu.ntnu.idatt2105.backend.enums.Role;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.security.JWTService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
@RequiredArgsConstructor
public class TestService {

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

    private String userToken;
    private String adminToken;

    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    /*public TestService() {

        var savedUser = userRepository.save(user);
        userToken = jwtService.generateToken(user);


        var savedAdmin = userRepository.save(admin);
        adminToken = jwtService.generateToken(admin);
    }*/


}
