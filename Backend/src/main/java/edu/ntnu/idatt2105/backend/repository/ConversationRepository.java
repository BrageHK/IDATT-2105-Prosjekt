package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.Conversation;
import edu.ntnu.idatt2105.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The repository for the conversation entity. This repository is used to access the database.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByBuyerAndSeller(User Buyer, User Seller);
}
