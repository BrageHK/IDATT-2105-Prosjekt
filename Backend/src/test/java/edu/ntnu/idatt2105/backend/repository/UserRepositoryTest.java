package edu.ntnu.idatt2105.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2105.backend.enums.Role;
import edu.ntnu.idatt2105.backend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test.test@test.test");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword("test");
        user.setRole(Role.USER);
        user.setAddress("Testveien 1");
        user.setPhoneNumber(12345678L);

        entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findByEmail(user.getEmail()).get();

        assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setEmail("test.test@test.test");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword("test");
        user.setRole(Role.USER);
        user.setAddress("Testveien 1");
        user.setPhoneNumber(12345678L);

        entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findById(user.getId()).get();

        assertEquals(user.getId(), found.getId());
    }

}
