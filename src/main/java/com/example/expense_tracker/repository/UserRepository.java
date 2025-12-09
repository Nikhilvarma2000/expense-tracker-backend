package com.example.expense_tracker.repository;

import com.example.expense_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Used for login & JWT authentication
    Optional<User> findByEmailIgnoreCase(String email);

    // ✅ Used during registration
    boolean existsByEmailIgnoreCase(String email);
}
