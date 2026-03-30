package com.mora.angkorleving_backend.DTOs.request;


import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username;
    private String phone;
    private String profileImage;

    private String role; // ADMIN, TENANT, USER

    private String password;
}

