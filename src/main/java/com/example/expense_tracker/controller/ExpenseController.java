package com.example.expense_tracker.controller;

import com.example.expense_tracker.dto.ExpenseRequest;
import com.example.expense_tracker.dto.ExpenseResponse;
import com.example.expense_tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveExpense(request));
    }

    // ✅ READ ALL (JWT REQUIRED)
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpenses() {
        return ResponseEntity.ok(service.getAllExpenses());
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {

        return ResponseEntity.ok(service.updateExpense(id, request));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        service.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ FILTER BY YEAR & MONTH
    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseResponse>> filterByMonth(
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(service.getExpensesByMonth(year, month));
    }
}
