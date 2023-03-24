package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.Repository.ConversationRepository;
import edu.ntnu.idatt2105.backend.Repository.MessageRepository;
import edu.ntnu.idatt2105.backend.Repository.UserRepository;
import edu.ntnu.idatt2105.backend.exception.ConversationNotFoundException;
import edu.ntnu.idatt2105.backend.exception.UserNotFoundException;
import edu.ntnu.idatt2105.backend.model.Conversation;
import edu.ntnu.idatt2105.backend.model.Message;
import edu.ntnu.idatt2105.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConversationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Conversation getOrCreateConversation(Long buyerId, Long sellerId) throws Exception {
        User buyer = userRepository.findById(buyerId).orElseThrow(() -> new Exception("Buyer not found"));
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new Exception("Seller not found"));

        return conversationRepository.findByBuyerAndSeller(buyer, seller)
                .orElseGet(() -> createNewConversation(buyer, seller));
    }

    private Conversation createNewConversation(User buyer, User seller) {
        Conversation conversation = Conversation.builder()
                .buyer(buyer)
                .seller(seller)
                .build();
        return conversationRepository.save(conversation);
    }

    public Message addMessageToConversation(Long conversationId, Long senderId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setSender(sender);
        message.setConversation(conversation);

        return messageRepository.save(message);
    }

}
