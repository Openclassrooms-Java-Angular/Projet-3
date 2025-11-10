package com.jflament.rental.service;

import com.jflament.rental.dto.RentalRequest;
import com.jflament.rental.entity.Rental;
import com.jflament.rental.entity.User;
import com.jflament.rental.repository.RentalRepository;
import com.jflament.rental.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Optional<Rental> findById(Long id) {
        return rentalRepository.findById(id);
    }

    public Rental create(RentalRequest request, User owner) {
        Rental rental = new Rental();
        rental.setName(request.getName());
        rental.setSurface(request.getSurface());
        rental.setPrice(request.getPrice());
        rental.setPicture(request.getPicture());
        rental.setDescription(request.getDescription());
        rental.setOwner(owner);

        return rentalRepository.save(rental);
    }

    public Optional<Rental> update(Long id, RentalRequest request, User owner) {
        return rentalRepository.findById(id).map(rental -> {
            // vérifier que l'utilisateur est bien le propriétaire
            if (!rental.getOwner().getId().equals(owner.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas le propriétaire de ce bien");
            }

            rental.setName(request.getName());
            rental.setSurface(request.getSurface());
            rental.setPrice(request.getPrice());
            rental.setPicture(request.getPicture());
            rental.setDescription(request.getDescription());
            rental.setUpdatedAt(LocalDateTime.now());

            return rentalRepository.save(rental);
        });
    }
}
