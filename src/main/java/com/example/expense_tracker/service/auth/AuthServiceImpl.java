package com.example.expense_tracker.service.auth;

import com.example.expense_tracker.dto.auth.LoginRequest;
import com.example.expense_tracker.dto.auth.RegisterRequest;
import com.example.expense_tracker.entity.User;
import com.example.expense_tracker.exception.ResourceNotFoundException;
import com.example.expense_tracker.repository.UserRepository;
import com.example.expense_tracker.security.jwt.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ✅ REGISTER USER
    @Override
    public void register(RegisterRequest request) {

        String email = request.getEmail()
                .toLowerCase()
                .trim();

        // ✅ Prevent duplicate email (case-insensitive)
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword())) // ✅ hashed
                .role("USER")
                .build();

        userRepository.save(user);
    }

    // ✅ LOGIN → RETURN JWT TOKEN
    @Override
    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid email or password"));

        // ✅ Password check
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())
        ) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        // ✅ Generate JWT
        return jwtUtil.generateToken(user.getEmail());
    }
}
