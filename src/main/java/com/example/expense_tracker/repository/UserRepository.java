package com.example.expense_tracker.repository;

import com.example.expense_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Fetch user by email (case-insensitive) – used for login & JWT auth
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Check if email already exists (case-insensitive) – used during register
     */
    boolean existsByEmailIgnoreCase(String email);
}
