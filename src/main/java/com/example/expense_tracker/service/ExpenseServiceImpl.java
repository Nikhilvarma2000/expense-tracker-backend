package com.example.expense_tracker.service;

import com.example.expense_tracker.dto.ExpenseRequest;
import com.example.expense_tracker.dto.ExpenseResponse;
import com.example.expense_tracker.entity.Expense;
import com.example.expense_tracker.exception.ResourceNotFoundException;
import com.example.expense_tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseServiceImpl(ExpenseRepository repository) {
        this.repository = repository;
    }

    // ✅ CREATE
    @Override
    public ExpenseResponse saveExpense(ExpenseRequest request) {

        LocalDate expenseDate = request.getExpenseDate() != null
                ? request.getExpenseDate()
                : LocalDate.now();   // ✅ FIX

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .category(request.getCategory())
                .expenseDate(expenseDate)
                .build();

        Expense saved = repository.save(expense);
        return mapToResponse(saved);
    }

    // ✅ READ ALL
    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ UPDATE
    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {

        Expense existing = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found with id: " + id));

        existing.setTitle(request.getTitle());
        existing.setAmount(request.getAmount());
        existing.setCategory(request.getCategory());

        LocalDate expenseDate = request.getExpenseDate() != null
                ? request.getExpenseDate()
                : existing.getExpenseDate();

        existing.setExpenseDate(expenseDate);

        Expense updated = repository.save(existing);
        return mapToResponse(updated);
    }

    // ✅ DELETE
    @Override
    public void deleteExpense(Long id) {

        Expense existing = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found with id: " + id));

        repository.delete(existing);
    }

    // ✅ FILTER BY MONTH
    @Override
    public List<ExpenseResponse> getExpensesByMonth(int year, int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return repository
                .findByExpenseDateBetween(startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ COMMON MAPPER
    private ExpenseResponse mapToResponse(Expense e) {
        return new ExpenseResponse(
                e.getId(),
                e.getTitle(),
                e.getAmount(),
                e.getCategory(),
                e.getExpenseDate()
        );
    }
}
