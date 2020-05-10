package com.balik.chat.controller;


import com.balik.chat.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/chat/public")
    public Message sendMessage(@Payload Message chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/chat/public")
    public Message addUser(@Payload Message chatMessage,
                           SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        return chatMessage;
    }

}
