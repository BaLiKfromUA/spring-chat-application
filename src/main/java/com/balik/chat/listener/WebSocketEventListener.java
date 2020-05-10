package com.balik.chat.listener;

import com.balik.chat.model.Message;
import com.balik.chat.model.MessageType;
import com.balik.chat.repo.MessageRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final MessageRepo messageRepo;

    @Autowired
    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate, MessageRepo messageRepo) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepo = messageRepo;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");

        if (username != null) {
            logger.info("User Disconnected : " + username);

            Message chatMessage = new Message();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(username);

            messageRepo.save(chatMessage);

            messagingTemplate.convertAndSend("/chat/public", chatMessage);
        }
    }

}
