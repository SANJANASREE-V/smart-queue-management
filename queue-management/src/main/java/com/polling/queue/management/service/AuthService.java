package com.polling.queue.management.service;

import com.polling.queue.management.dto.AuthResponse;
import com.polling.queue.management.dto.LoginRequest;
import com.polling.queue.management.dto.RegisterRequest;
import com.polling.queue.management.model.User;
import com.polling.queue.management.repository.UserRepository;
import com.polling.queue.management.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.existsByVoterId(request.getVoterId())) {
            throw new RuntimeException("Voter ID already registered");
        }

        User user = new User(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getFullName(),
            request.getVoterId(),
            request.getRole()
        );
        user.setAssignedBoothId(request.getAssignedBoothId());

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(
            savedUser.getEmail(), savedUser.getRole(), savedUser.getId());

        return new AuthResponse(
            token, savedUser.getRole(), savedUser.getId(),
            savedUser.getFullName(), savedUser.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(
            user.getEmail(), user.getRole(), user.getId());

        return new AuthResponse(
            token, user.getRole(), user.getId(),
            user.getFullName(), user.getEmail());
    }

    public User registerAdmin(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email already registered");
    }

    User admin = new User(
        request.getEmail(),
        passwordEncoder.encode(request.getPassword()),
        request.getFullName(),
        request.getVoterId(),
        request.getRole()  // ← gets role from request instead of hardcoding
    );

    userRepository.save(admin);
    return admin;
}
}

