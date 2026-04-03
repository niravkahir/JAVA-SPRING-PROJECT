package com.nirav.expense_tracker.repository;

import com.nirav.expense_tracker.entity.Budget;
import com.nirav.expense_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserAndMonth(User user, String month);
}