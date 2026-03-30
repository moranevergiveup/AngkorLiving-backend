package com.mora.angkorleving_backend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mora.angkorleving_backend.DTOs.request.UserUpdateRequest;
import com.mora.angkorleving_backend.DTOs.response.UserRespone;
import com.mora.angkorleving_backend.Repository.UserRepository;
import com.mora.angkorleving_backend.model.Role;
import com.mora.angkorleving_backend.model.User;
import com.mora.angkorleving_backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private  Cloudinary cloudinary;

    @Override
//    public List<UserRespone> getAllUsers() {
//        return userRepository.findAll()
//                .stream()
//                .map(u -> new UserRespone(
//                        u.getId(),
//                        u.getUsername(),
//                        u.getEmail(),
//                        u.getRole().name(),
//                        u.getPhone(),
//                        u.getProfileImage()
//                ))
//                .toList();
//    }
    public List<UserRespone> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(u -> new UserRespone(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getRole().name(),
                        u.getPhone(),
                        u.getProfileImage()
                ))
                .toList();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public User updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // Validation
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }
        if (request.getRole() != null) {
            try {
                user.setRole(Role.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role: " + request.getRole());
            }
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User getProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

//    @Override
//    public User updateProfile(String email, UserUpdateRequest request) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
//
//        // Role-check: only TENANT or USER can update their own profile
//        if (!(user.getRole() == Role.TENANT )) {
//            throw new AccessDeniedException("Only TENANT or USER can update profile");
//        }
//
//        // Validation
//        if (request.getUsername() != null && !request.getUsername().isBlank()) {
//            user.setUsername(request.getUsername());
//        }
//        if (request.getPhone() != null) {
//            user.setPhone(request.getPhone());
//        }
//        if (request.getProfileImage() != null) {
//            user.setProfileImage(request.getProfileImage());
//        }
//
//        return userRepository.save(user);
//    }
    @Override
    public UserRespone updateProfile(String email, UserUpdateRequest request, MultipartFile imageFile) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        // Role-check: only TENANT (or USER if you want to allow both)
        if (!(user.getRole() == Role.TENANT)) {
            throw new AccessDeniedException("Only TENANT can update profile");
        }

        // Update fields
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        // ✅ Upload profile image to Cloudinary
        if (imageFile != null && !imageFile.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");
            user.setProfileImage(imageUrl);
        }

        User updated = userRepository.save(user);
        return toResponse(updated);
    }

    private UserRespone toResponse(User user) {
        return new UserRespone(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getPhone(),
                user.getProfileImage()
        );
    }
//    public User updateProfile(String email, UserUpdateRequest request, MultipartFile imageFile) throws IOException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
//
//        // Role-check: only TENANT can update their own profile
//        if (!(user.getRole() == Role.TENANT)) {
//            throw new AccessDeniedException("Only TENANT can update profile");
//        }
//
//        // Validation
//        if (request.getUsername() != null && !request.getUsername().isBlank()) {
//            user.setUsername(request.getUsername());
//        }
//        if (request.getPhone() != null) {
//            user.setPhone(request.getPhone());
//        }
//
//        // ✅ Handle profile image upload
//        if (imageFile != null && !imageFile.isEmpty()) {
//            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
//            String imageUrl = (String) uploadResult.get("url");
//            user.setProfileImage(imageUrl);
//        }
//
//        return userRepository.save(user);
//    }

    @Override
    public List<UserRespone> getAllTenants() {
        return userRepository.findByRole(Role.TENANT)
                .stream()
                .map(u -> new UserRespone(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getRole().name(),
                        u.getPhone(),
                        u.getProfileImage()
                ))
                .toList();
    }

    @Override
//    public User updateProfile( UserUpdateRequest request) {
//
//
//        if (request.getUsername() != null && !request.getUsername().isBlank()) {
//            user.setUsername(request.getUsername());
//        }
//        if (request.getPhone() != null) {
//            user.setPhone(request.getPhone());
//        }
//        if (request.getProfileImage() != null) {
//            user.setProfileImage(request.getProfileImage());
//        }
//        if (request.getRole() != null) {
//            try {
//                user.setRole(Role.valueOf(request.getRole().toUpperCase()));
//            } catch (IllegalArgumentException e) {
//                throw new RuntimeException("Invalid role: " + request.getRole());
//            }
//        }
//
//        return userRepository.save(user);
//    }

//    @Override
    public User resetPassword(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // ✅ Set default password
        user.setPassword(passwordEncoder.encode("AngkorLiving@123"));

        return userRepository.save(user);
    }
}


