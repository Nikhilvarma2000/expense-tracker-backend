package com.example.expense_tracker.service;

import com.example.expense_tracker.dto.ExpenseRequest;
import com.example.expense_tracker.dto.ExpenseResponse;
import com.example.expense_tracker.entity.Expense;
import com.example.expense_tracker.entity.User;
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
    public ExpenseResponse saveExpense(ExpenseRequest request, User user) {

        LocalDate expenseDate = request.getExpenseDate() != null
                ? request.getExpenseDate()
                : LocalDate.now();

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .category(request.getCategory())
                .expenseDate(expenseDate)
                .user(user)
                .build();

        Expense saved = repository.save(expense);
        return mapToResponse(saved);
    }

    // ✅ READ ALL (USER ONLY)
    @Override
    public List<ExpenseResponse> getAllExpenses(User user) {
        return repository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();   // ✅ Java 16+
    }

    // ✅ UPDATE (VERIFY OWNER)
    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request, User user) {

        Expense existing = repository.findById(id)
                .filter(expense -> expense.getUser().getId().equals(user.getId()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found or access denied"));

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

    // ✅ DELETE (VERIFY OWNER)
    @Override
    public void deleteExpense(Long id, User user) {

        Expense existing = repository.findById(id)
                .filter(expense -> expense.getUser().getId().equals(user.getId()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found or access denied"));

        repository.delete(existing);
    }

    // ✅ FILTER BY MONTH (USER ONLY)
    @Override
    public List<ExpenseResponse> getExpensesByMonth(int year, int month, User user) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return repository
                .findByUserAndExpenseDateBetween(user, startDate, endDate)
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
