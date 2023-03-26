package edu.ntnu.idatt2105.backend.repository;

import edu.ntnu.idatt2105.backend.model.Conversation;
import edu.ntnu.idatt2105.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationOrderByTimestamp(Conversation conversation);
}