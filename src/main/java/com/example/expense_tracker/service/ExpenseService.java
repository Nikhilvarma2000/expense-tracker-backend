package com.example.expense_tracker.service;

import com.example.expense_tracker.dto.ExpenseRequest;
import com.example.expense_tracker.dto.ExpenseResponse;

import java.util.List;

public interface ExpenseService {

    // ✅ CREATE
    ExpenseResponse saveExpense(ExpenseRequest request);

    // ✅ READ ALL
    List<ExpenseResponse> getAllExpenses();

    // ✅ UPDATE
    ExpenseResponse updateExpense(Long id, ExpenseRequest request);

    // ✅ DELETE
    void deleteExpense(Long id);

    // ✅ FILTER BY YEAR & MONTH
    List<ExpenseResponse> getExpensesByMonth(int year, int month);
}
