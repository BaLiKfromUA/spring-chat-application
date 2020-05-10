package com.balik.chat.controller;

import com.balik.chat.model.Message;
import com.balik.chat.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatRestController {
    private final MessageRepo messageRepo;

    @Autowired
    public ChatRestController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping("/history")
    List<Message> getChatHistory(){
        return  messageRepo.findAll();
    }
}
