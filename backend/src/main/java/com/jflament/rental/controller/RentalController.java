package com.jflament.rental.controller;

import com.jflament.rental.dto.RentalRequest;
import com.jflament.rental.dto.RentalResponse;
import com.jflament.rental.entity.Rental;
import com.jflament.rental.entity.User;
import com.jflament.rental.security.CustomUserDetails;
import com.jflament.rental.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<RentalResponse>> getAll() {
        List<RentalResponse> rentals = rentalService.getAllRentals()
                .stream()
                .map(RentalResponse::new)
                .toList();
        return ResponseEntity.ok(rentals);
    }
    // GET /api/rentals/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Long id) {
        return rentalService.findById(id)
                .map(r -> ResponseEntity.ok(new RentalResponse(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/rentals
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalResponse> createRental(@RequestBody RentalRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User owner = userDetails.getUser(); // récupère l’entité User du JWT
        Rental rental = rentalService.create(request, owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RentalResponse(rental));
    }

    // PUT /api/rentals/{id}
    @PutMapping("/{id}")
    public ResponseEntity<RentalResponse> updateRental(@PathVariable Long id,
                                                       @RequestBody RentalRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        User owner = userDetails.getUser();

        return rentalService.update(id, request, owner)
                .map(r -> ResponseEntity.ok(new RentalResponse(r)))
                .orElse(ResponseEntity.notFound().build());
    }
}
