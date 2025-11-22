package com.jflament.rental.service;

import com.jflament.rental.dto.RentalRequest;
import com.jflament.rental.entity.Rental;
import com.jflament.rental.entity.User;
import com.jflament.rental.repository.RentalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final S3StorageService s3StorageService;

    public RentalService(RentalRepository rentalRepository, S3StorageService s3StorageService) {
        this.rentalRepository = rentalRepository;
        this.s3StorageService = s3StorageService;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Optional<Rental> findById(Long id) {
        return rentalRepository.findById(id);
    }

    public void create(RentalRequest request, User owner) {
        String fileUrl = null;
        if (request.getPicture() != null && !request.getPicture().isEmpty()) {
            fileUrl = safeUploadFile(request.getPicture());
        }

        Rental rental = new Rental();
        rental.setName(request.getName());
        rental.setSurface(request.getSurface());
        rental.setPrice(request.getPrice());
        rental.setPicture(fileUrl);
        rental.setDescription(request.getDescription());
        rental.setOwner(owner);

        rentalRepository.save(rental);
    }

    public void createFromMultipart(RentalRequest request, User owner) {
        create(request, owner);
    }

    public Optional<Rental> update(Long id, RentalRequest request, User owner) {
        return rentalRepository.findById(id).map(rental -> {
            // vérifier que l'utilisateur est bien le propriétaire
            if (!rental.getOwner().getId().equals(owner.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas le propriétaire de ce bien");
            }

            String fileUrl = null;
            if (request.getPicture() != null && !request.getPicture().isEmpty()) {
                fileUrl = safeUploadFile(request.getPicture());
            }

            rental.setName(request.getName());
            rental.setSurface(request.getSurface());
            rental.setPrice(request.getPrice());
            rental.setPicture(fileUrl);
            rental.setDescription(request.getDescription());
            rental.setUpdatedAt(LocalDateTime.now());

            return rentalRepository.save(rental);
        });
    }

    public boolean updateFromMultipart(Long id, RentalRequest request, User owner) {
        Optional<Rental> optional = rentalRepository.findById(id);
        if (optional.isEmpty()) {
            return false;
        }

        Rental rental = optional.get();

        // vérifier que l’utilisateur est bien propriétaire
        if (!rental.getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("Not your rental");
        }

        rental.setName(request.getName());
        rental.setSurface(request.getSurface());
        rental.setPrice(request.getPrice());
        rental.setDescription(request.getDescription());

        // si une nouvelle image est fournie → on remplace
        if (request.getPicture() != null && !request.getPicture().isEmpty()) {
            String url = safeUploadFile(request.getPicture());
            rental.setPicture(url);
        }

        rentalRepository.save(rental);
        return true;
    }

    private String safeUploadFile(MultipartFile file) {
        try {
            return s3StorageService.uploadFile(file);
        } catch (S3Exception e) {
            System.err.println("S3 Error:");
            System.err.println("Status code: " + e.statusCode());
            System.err.println("AWS Error Code: " + e.awsErrorDetails().errorCode());
            System.err.println("AWS Error Message: " + e.awsErrorDetails().errorMessage());
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur S3 : " + e.awsErrorDetails().errorMessage()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de l'upload du fichier : " + e.getMessage()
            );
        }
    }
}
