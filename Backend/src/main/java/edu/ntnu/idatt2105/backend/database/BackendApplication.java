package edu.ntnu.idatt2105.backend;

import edu.ntnu.idatt2105.backend.database.User;
import edu.ntnu.idatt2105.backend.database.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }


    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            User u = new User();
            userRepository.save(u);
        };
    }
}
