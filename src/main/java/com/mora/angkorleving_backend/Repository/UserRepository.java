package com.mora.angkorleving_backend.Repository;


import com.mora.angkorleving_backend.model.Role;
import com.mora.angkorleving_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by username
    Optional<User> findByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Check if username exists
    boolean existsByUsername(String username);
    List<User> findByRole(Role role);
}

