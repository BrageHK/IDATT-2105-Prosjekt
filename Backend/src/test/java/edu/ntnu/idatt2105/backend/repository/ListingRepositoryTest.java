package edu.ntnu.idatt2105.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.idatt2105.backend.enums.Role;
import edu.ntnu.idatt2105.backend.model.Category;
import edu.ntnu.idatt2105.backend.model.Listing;
import edu.ntnu.idatt2105.backend.model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ListingRepository listingRepository;

    private static User user = new User();
    private static Category category = new Category();

    @BeforeAll
    void createUserAndCategory() {
        Logger logger = org.slf4j.LoggerFactory.getLogger(ListingRepositoryTest.class);
        logger.info("Hei" + user.toString());
    }

    @Test
    public void testFindById() {
        user = User.builder()
                .email("test.test@test.test")
                .firstName("Test")
                .lastName("Test")
                .password("test")
                .role(Role.USER)
                .address("Testveien 1")
                .phoneNumber(12345678L)
                .build();

        category = Category.builder()
                .name("Test")
                .build();

        entityManager.persist(user);
        entityManager.persist(category);
        entityManager.flush();

        Listing listing = Listing.builder()
                .briefDescription("Test")
                .description("Test")
                .latitude(1.0)
                .longitude(1.0)
                .dateCreated(LocalDateTime.now())
                .isSold(false)
                .address("Testveien 1")
                .price(1000)
                .category(category)
                .owner(user)
                .build();

        entityManager.persist(listing);
        entityManager.flush();

        Listing found = listingRepository.findById(listing.getId()).get();

        assertEquals(listing.getId(), found.getId());
    }

}
