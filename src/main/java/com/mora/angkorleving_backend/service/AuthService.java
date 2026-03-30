package com.mora.angkorleving_backend.service;


import com.mora.angkorleving_backend.DTOs.request.LoginRequest;
import com.mora.angkorleving_backend.DTOs.request.RegisterRequest;
import com.mora.angkorleving_backend.DTOs.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}

