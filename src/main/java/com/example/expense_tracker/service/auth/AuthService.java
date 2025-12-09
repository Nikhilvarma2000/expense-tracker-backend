package com.example.expense_tracker.service.auth;

import com.example.expense_tracker.dto.auth.LoginRequest;
import com.example.expense_tracker.dto.auth.RegisterRequest;

public interface AuthService {

    /**
     * Registers a new user with email & password
     * - Email must be unique
     * - Password will be stored in hashed form
     */
    void register(RegisterRequest request);

    /**
     * Authenticates user and returns JWT token
     */
    String login(LoginRequest request);
}
