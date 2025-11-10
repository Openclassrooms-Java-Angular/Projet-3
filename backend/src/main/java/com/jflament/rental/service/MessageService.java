package com.jflament.rental.service;

import com.jflament.rental.dto.MessageRequest;
import com.jflament.rental.entity.Message;
import com.jflament.rental.entity.Rental;
import com.jflament.rental.entity.User;
import com.jflament.rental.repository.MessageRepository;
import com.jflament.rental.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;

    public MessageService(MessageRepository messageRepository, RentalRepository rentalRepository) {
        this.messageRepository = messageRepository;
        this.rentalRepository = rentalRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Message create(MessageRequest request, User user) {
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        Message message = new Message();
        message.setRental(rental);
        message.setUser(user);
        message.setMessage(request.getMessage());

        LocalDateTime now = LocalDateTime.now();
        message.setCreatedAt(now);
        message.setUpdatedAt(now);

        return messageRepository.save(message);
    }
}
