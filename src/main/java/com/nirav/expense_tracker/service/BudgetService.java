package com.nirav.expense_tracker.service;

import com.nirav.expense_tracker.entity.Budget;
import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.YearMonth;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseService expenseService;

    public Budget setBudget(Double monthlyLimit, User user) {
        String currentMonth = YearMonth.now().toString();

        Budget budget = budgetRepository.findByUserAndMonth(user, currentMonth)
                .orElse(new Budget());

        budget.setMonthlyLimit(monthlyLimit);
        budget.setMonth(currentMonth);
        budget.setUser(user);

        return budgetRepository.save(budget);
    }

    public Budget getUserBudget(User user) {
        String currentMonth = YearMonth.now().toString();
        return budgetRepository.findByUserAndMonth(user, currentMonth)
                .orElse(null);
    }

    public String checkBudgetStatus(User user) {
        Budget budget = getUserBudget(user);
        if (budget == null) {
            return "No budget set for this month";
        }

        Double totalExpenses = expenseService.getTotalExpenses(user);
        double remaining = budget.getMonthlyLimit() - totalExpenses;

        if (remaining < 0) {
            return String.format("Budget exceeded! Limit: %.2f, Spent: %.2f, Over budget: %.2f",
                    budget.getMonthlyLimit(), totalExpenses, Math.abs(remaining));
        } else if (remaining < budget.getMonthlyLimit() * 0.2) {
            return String.format("Warning: Only %.2f remaining of your %.2f budget",
                    remaining, budget.getMonthlyLimit());
        } else {
            return String.format("On track! Spent: %.2f of %.2f budget. Remaining: %.2f",
                    totalExpenses, budget.getMonthlyLimit(), remaining);
        }
    }
}