package com.jflament.rental.controller;

import com.jflament.rental.dto.MessageRequest;
import com.jflament.rental.dto.MessageResponse;
import com.jflament.rental.entity.Message;
import com.jflament.rental.entity.User;
import com.jflament.rental.security.CustomUserDetails;
import com.jflament.rental.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Message message = messageService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(message));
    }
}
