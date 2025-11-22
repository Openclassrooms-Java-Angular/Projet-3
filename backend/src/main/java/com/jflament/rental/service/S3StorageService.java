package com.jflament.rental.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${s3.bucket.url}")
    private String bucketUrl;

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value("${s3.bucket.folder}")
    private String folder;

    public S3StorageService() {
        this.s3Client = S3Client.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(ProfileCredentialsProvider.create("dev"))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "file";
        }

        // Générer un nom unique en conservant l'extension
        String uniqueFilename = generateUniqueFileName(originalFilename);

        // Clé S3 (dossier + nom unique)
        String key = folder + uniqueFilename;

        System.out.println("Uploading file: " + uniqueFilename + ", size: " + file.getSize());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest,
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

        // nom du fichier enregistré sur S3, sans répertoire devant
        return uniqueFilename;
    }

    public String generateUniqueFileName(String fileName) {
        if (fileName == null) {
            fileName = "file";
        }

        // extraire l'extension
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = fileName.substring(dotIndex); // inclut le '.'
        }

        // générer un hash du nom original
        String hash = DigestUtils.md5DigestAsHex(fileName.getBytes());

        // nom unique final
        return hash + "_" + UUID.randomUUID() + extension;
    }

    public String getFileUrl(String key) {
        if (key == null || key.isEmpty()) return null;
        return bucketUrl + folder + key;
    }
}