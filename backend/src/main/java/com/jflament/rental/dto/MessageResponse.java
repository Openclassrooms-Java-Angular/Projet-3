package com.jflament.rental.dto;

import com.jflament.rental.entity.Message;
import java.time.format.DateTimeFormatter;

public class MessageResponse {
    private Long id;
    private Long rentalId;
    private Long userId;
    private String message;
    private String createdAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public MessageResponse(Message messageEntity) {
        this.id = messageEntity.getId();
        this.rentalId = messageEntity.getRental().getId();
        this.userId = messageEntity.getUser().getId();
        this.message = messageEntity.getMessage();
        this.createdAt = messageEntity.getCreatedAt() != null
                ? messageEntity.getCreatedAt().format(FORMATTER)
                : null;
    }

    // Getters
    public Long getId() { return id; }
    public Long getRentalId() { return rentalId; }
    public Long getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getCreatedAt() { return createdAt; }
}
