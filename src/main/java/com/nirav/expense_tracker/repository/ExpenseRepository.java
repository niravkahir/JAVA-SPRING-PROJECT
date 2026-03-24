package com.nirav.expense_tracker.repository;

import com.nirav.expense_tracker.entity.Expense;
import com.nirav.expense_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user")
    Double getTotalExpensesByUser(@Param("user") User user);

    @Query("SELECT e.category.name, SUM(e.amount) FROM Expense e WHERE e.user = :user GROUP BY e.category.name")
    List<Object[]> getCategoryWiseExpenses(@Param("user") User user);
}