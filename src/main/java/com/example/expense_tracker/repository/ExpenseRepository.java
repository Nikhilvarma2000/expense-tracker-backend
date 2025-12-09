package com.example.expense_tracker.repository;

import com.example.expense_tracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // ✅ Used for month-based filtering
    List<Expense> findByExpenseDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}
