package com.jflament.rental.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    // Clé secrète générée une seule fois
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Durée du token en millisecondes
    private static final long EXPIRATION_TIME = 3600_000; // 1h

    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)                    // utilisateur
                .setIssuedAt(new Date())                 // date d’émission
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // date d’expiration
                .claim("role", role)                     // rôle custom
                .signWith(key)                           // signature
                .compact();
    }

    public static SecretKey getKey() {
        return key;
    }
}
