package com.jflament.rental.controller;

import com.jflament.rental.dto.LoginRequest;
import com.jflament.rental.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = JwtUtil.generateToken(request.getEmail(), "user");

        // TODO: vérifier avec la base si besoin
        return Map.of("token", token);
    }
}