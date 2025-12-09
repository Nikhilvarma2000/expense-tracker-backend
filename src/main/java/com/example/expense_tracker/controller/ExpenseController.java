package com.example.expense_tracker.controller;

import com.example.expense_tracker.dto.ExpenseRequest;
import com.example.expense_tracker.dto.ExpenseResponse;
import com.example.expense_tracker.entity.User;
import com.example.expense_tracker.repository.UserRepository;
import com.example.expense_tracker.security.jwt.JwtUtil;
import com.example.expense_tracker.service.ExpenseService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService service;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public ExpenseController(
            ExpenseService service,
            JwtUtil jwtUtil,
            UserRepository userRepository) {

        this.service = service;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request,
            HttpServletRequest httpRequest) {

        User user = getLoggedInUser(httpRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveExpense(request, user));
    }

    // ✅ READ ALL (USER ONLY)
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpenses(
            HttpServletRequest httpRequest) {

        User user = getLoggedInUser(httpRequest);
        return ResponseEntity.ok(service.getAllExpenses(user));
    }

    // ✅ UPDATE (OWNER ONLY)
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request,
            HttpServletRequest httpRequest) {

        User user = getLoggedInUser(httpRequest);
        return ResponseEntity.ok(service.updateExpense(id, request, user));
    }

    // ✅ DELETE (OWNER ONLY)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        User user = getLoggedInUser(httpRequest);
        service.deleteExpense(id, user);
        return ResponseEntity.noContent().build();
    }

    // ✅ FILTER BY YEAR & MONTH (USER ONLY)
    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponse>> filterByMonth(
            @RequestParam int year,
            @RequestParam int month,
            HttpServletRequest httpRequest) {

        User user = getLoggedInUser(httpRequest);
        return ResponseEntity.ok(service.getExpensesByMonth(year, month, user));
    }

    // ✅ COMMON METHOD — extract User from JWT
    private User getLoggedInUser(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found for email: " + email));
    }
}
