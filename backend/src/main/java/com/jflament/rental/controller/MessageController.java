package com.jflament.rental.controller;

import com.jflament.rental.dto.MessageRequest;
import com.jflament.rental.entity.User;
import com.jflament.rental.security.CustomUserDetails;
import com.jflament.rental.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createMessage(
            @RequestBody MessageRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (request.getRentalId() == null || request.getMessage() == null || userDetails == null) {
            return ResponseEntity.badRequest().body(Map.of());
        }

        User user = userDetails.getUser();
        messageService.create(request, user);

        return ResponseEntity.ok(Map.of("message", "Message send with success"));
    }
}
