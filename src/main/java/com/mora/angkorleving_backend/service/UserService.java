package com.mora.angkorleving_backend.service;

import com.mora.angkorleving_backend.DTOs.request.UserUpdateRequest;
import com.mora.angkorleving_backend.DTOs.response.UserRespone;
import com.mora.angkorleving_backend.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<UserRespone> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    List<UserRespone> getAllTenants();
    User getProfile(String email);
    UserRespone updateProfile(String email, UserUpdateRequest request, MultipartFile imageFile)throws IOException;
    // 👉 Update profile only
//    User updateProfile(UserUpdateRequest request);
//    User getProfile(Long id);
    // 👉 Reset password only
    User resetPassword(Long id);
}

