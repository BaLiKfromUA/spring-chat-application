package com.balik.chat.controller;


import com.balik.chat.model.Message;
import com.balik.chat.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class ChatActionController {
    final private MessageRepo messageRepo;

    @Autowired
    public ChatActionController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/chat/public")
    public Message sendMessage(@Payload Message chatMessage) {
        messageRepo.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/chat/public")
    public Message addUser(@Payload Message chatMessage,
                           SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        messageRepo.save(chatMessage);
        return chatMessage;
    }

}
