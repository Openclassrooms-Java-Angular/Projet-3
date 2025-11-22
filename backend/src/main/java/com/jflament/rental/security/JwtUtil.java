package com.jflament.rental.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

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

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // parse ok = token valide
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extraire l'email (ou username) depuis le token
    public static String extractEmail(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // normalement on met l'email dans "subject"
        } catch (Exception e) {
            return null;
        }
    }
}
