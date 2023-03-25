package edu.ntnu.idatt2105.backend.Repository;

import edu.ntnu.idatt2105.backend.model.Conversation;
import edu.ntnu.idatt2105.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByBuyerAndSeller(User Buyer, User Seller);
}
