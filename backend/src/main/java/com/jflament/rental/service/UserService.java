package com.jflament.rental.service;

import com.jflament.rental.entity.User;
import com.jflament.rental.repository.UserRepository;
import com.jflament.rental.security.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User register(String name, String email, String rawPassword) throws Exception {
        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email déjà utilisé");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        return userRepository.save(user);
    }

    public String generateToken(User user) {
        return JwtUtil.generateToken(user.getEmail(), "user");
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
