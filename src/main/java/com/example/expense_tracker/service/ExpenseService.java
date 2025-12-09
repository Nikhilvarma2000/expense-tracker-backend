package com.example.expense_tracker.service;

import com.example.expense_tracker.dto.ExpenseRequest;
import com.example.expense_tracker.dto.ExpenseResponse;
import com.example.expense_tracker.entity.User;

import java.util.List;

public interface ExpenseService {

    // ✅ CREATE (for logged-in user)
    ExpenseResponse saveExpense(ExpenseRequest request, User user);

    // ✅ READ ALL (only logged-in user's expenses)
    List<ExpenseResponse> getAllExpenses(User user);

    // ✅ UPDATE (only if expense belongs to user)
    ExpenseResponse updateExpense(Long id, ExpenseRequest request, User user);

    // ✅ DELETE (only if expense belongs to user)
    void deleteExpense(Long id, User user);

    // ✅ FILTER BY YEAR & MONTH (user-specific)
    List<ExpenseResponse> getExpensesByMonth(int year, int month, User user);
}
