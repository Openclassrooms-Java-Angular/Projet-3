package com.jflament.rental.service;

import com.jflament.rental.dto.RentalRequest;
import com.jflament.rental.entity.Rental;
import com.jflament.rental.entity.User;
import com.jflament.rental.repository.RentalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
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

    public void createFromMultipart(RentalRequest request, User owner) {
        // gérer l'image si nécessaire
        create(request, owner);
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

    public boolean updateFromMultipart(Long id, String name, BigDecimal surface, BigDecimal price,
                                       String description, MultipartFile picture, User owner) {
        return update(id, new RentalRequest(name, surface, price, picture.getName(), description), owner)
                .map(rental -> {
                    if (picture != null) {
                        // gérer l'image si nécessaire
                    }
                    return true;
                })
                .orElse(false);
    }
}
