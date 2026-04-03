package com.mora.angkorleving_backend.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String username;
    private String email;
    private String phone;
    private String profileImage;
    private String Password;

    private List<RentalResponse> rentals;
}