package com.jflament.rental.controller;

import com.jflament.rental.dto.RentalRequest;
import com.jflament.rental.dto.RentalResponse;
import com.jflament.rental.entity.User;
import com.jflament.rental.security.CustomUserDetails;
import com.jflament.rental.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<RentalResponse>>> getAll() {
        List<RentalResponse> rentals = rentalService.getAllRentals()
                .stream()
                .map(RentalResponse::new)
                .toList();

        // on enveloppe la liste dans une map avec la clé "rentals"
        Map<String, List<RentalResponse>> response = Map.of("rentals", rentals);
        return ResponseEntity.ok(response);
    }


    // GET /api/rentals/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Long id) {
        return rentalService.findById(id)
                .map(r -> ResponseEntity.ok(new RentalResponse(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/rentals
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of());
        }

        // validation minimale
        if (name == null || description == null || picture == null) {
            return ResponseEntity.badRequest().body(Map.of());
        }

        rentalService.createFromMultipart(new RentalRequest(name, surface, price, picture.getName(), description), userDetails.getUser());

        return ResponseEntity.ok(Map.of("message", "Rental created !"));
    }

    // PUT /api/rentals/{id}
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateRental(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of());
        }

        User owner = userDetails.getUser();

        // Validation minimale si nécessaire
        if (name == null || description == null) {
            return ResponseEntity.badRequest().body(Map.of());
        }

        boolean updated = rentalService.updateFromMultipart(id, name, surface, price, description, picture, owner);

        if (updated) {
            return ResponseEntity.ok(Map.of("message", "Rental updated !"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
