package com.mora.angkorleving_backend.service.impl;

import com.mora.angkorleving_backend.DTOs.request.LoginRequest;
import com.mora.angkorleving_backend.DTOs.request.RegisterRequest;
import com.mora.angkorleving_backend.DTOs.response.AuthResponse;
import com.mora.angkorleving_backend.Repository.UserRepository;
import com.mora.angkorleving_backend.model.Role;
import com.mora.angkorleving_backend.model.User;
import com.mora.angkorleving_backend.security.JwtUtil;
import com.mora.angkorleving_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .phone(request.getPhone())
                .role(
                        request.getRole() != null
                                ? request.getRole()
                                : Role.TENANT // default role
                )
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getRole().name());
    }
}

