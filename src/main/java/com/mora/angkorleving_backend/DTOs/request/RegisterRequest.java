package com.mora.angkorleving_backend.DTOs.request;
import com.mora.angkorleving_backend.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Username is required")
    private String username;

    @Column(nullable=true)
    private String phone;
    @Enumerated(EnumType.STRING)
    @Column(nullable=true)
    private Role role;
}

