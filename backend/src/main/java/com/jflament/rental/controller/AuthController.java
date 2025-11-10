package com.jflament.rental.controller;

import com.jflament.rental.dto.JwtResponse;
import com.jflament.rental.dto.LoginRequest;
import com.jflament.rental.dto.RegisterRequest;
import com.jflament.rental.dto.UserResponse;
import com.jflament.rental.entity.User;
import com.jflament.rental.security.CustomUserDetails;
import com.jflament.rental.security.JwtUtil;
import com.jflament.rental.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request.getName(), request.getEmail(), request.getPassword());
            String token = userService.generateToken(user);

            // retourner le token au client
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = JwtUtil.generateToken(request.getEmail(), "user");
        return Map.of("token", token);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Utilisateur non authentifié");
        }
        User user = userDetails.getUser();
        return ResponseEntity.ok(new UserResponse(user));
    }
}