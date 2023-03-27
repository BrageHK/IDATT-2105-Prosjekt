package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.repository.ConversationRepository;
import edu.ntnu.idatt2105.backend.repository.MessageRepository;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.exception.ConversationNotFoundException;
import edu.ntnu.idatt2105.backend.exception.UserNotFoundException;
import edu.ntnu.idatt2105.backend.model.Conversation;
import edu.ntnu.idatt2105.backend.model.Message;
import edu.ntnu.idatt2105.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service class for conversation. This class is not finished as
 *
 * @author Brage H. Kvamme
 * @version 0.1
 */
@Service
public class ConversationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Get or create a conversation between two users. If the conversation does not exist, it will be created.
     * If the conversation exists, it will be returned. The conversation is created with the buyer as the first user
     * and the seller as the second user.
     *
     * @param buyerId id of the buyer
     * @param sellerId id of the seller
     * @return the conversation between the two users
     * @throws Exception if the buyer or seller does not exist
     */
    public Conversation getOrCreateConversation(Long buyerId, Long sellerId) throws Exception {
        User buyer = userRepository.findById(buyerId).orElseThrow(() -> new Exception("Buyer not found"));
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new Exception("Seller not found"));

        return conversationRepository.findByBuyerAndSeller(buyer, seller)
                .orElseGet(() -> createNewConversation(buyer, seller));
    }

    /**
     * Create a new conversation between two users.
     *
     * @param buyer The buyer
     * @param seller The seller
     * @return The new conversation
     */
    private Conversation createNewConversation(User buyer, User seller) {
        Conversation conversation = Conversation.builder()
                .buyer(buyer)
                .seller(seller)
                .build();
        return conversationRepository.save(conversation);
    }

    /**
     * Add a message to a conversation. The sender must be part of the conversation.
     *
     * @param conversationId id of the conversation
     * @param senderId id of the sender
     * @param content content of the message
     * @return the message
     */
    public Message addMessageToConversation(Long conversationId, Long senderId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(conversation.getBuyer() != sender && conversation.getSeller() != sender) {
            throw new UserNotFoundException("User not part of conversation");
        }

        Message message = Message.builder()
                .content(content)
                .timestamp(LocalDateTime.now())
                .sender(sender)
                .conversation(conversation)
                .build();

        return messageRepository.save(message);
    }

}
