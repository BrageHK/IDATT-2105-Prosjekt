package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private ConversationService conversationService;

    @MessageMapping("/send/message")
    public void sendMessage(String message) {
        // You can customize this method to handle the message as needed.
        // For example, you can save the message to the database or process it in some other way.
        // eg elske mat
        //conversationService.getOrCreateConversation("/topic/messages", message);
    }
}