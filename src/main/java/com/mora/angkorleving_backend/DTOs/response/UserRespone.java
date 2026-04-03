package com.mora.angkorleving_backend.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRespone {
    Long id;
    String username;
    String email;
    String role;
    String phone;
    String profileImage;
//    String password
}
