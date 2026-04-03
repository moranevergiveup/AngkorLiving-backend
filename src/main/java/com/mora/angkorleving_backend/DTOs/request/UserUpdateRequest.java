package com.mora.angkorleving_backend.DTOs.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateRequest {
    private String username;
    private String phone;
    private String profileImage;
    private String password;
}

