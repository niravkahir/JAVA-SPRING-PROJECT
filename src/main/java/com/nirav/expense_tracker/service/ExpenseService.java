package com.nirav.expense_tracker.service;

import com.nirav.expense_tracker.dto.CategoryWiseExpense;
import com.nirav.expense_tracker.dto.ExpenseDTO;
import com.nirav.expense_tracker.entity.Category;
import com.nirav.expense_tracker.entity.Expense;
import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.repository.CategoryRepository;
import com.nirav.expense_tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Expense addExpense(ExpenseDTO expenseDTO, User user) {
        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(expenseDTO.getDate());
        expense.setDescription(expenseDTO.getDescription());
        expense.setUser(user);
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    public List<Expense> getUserExpenses(User user) {
        return expenseRepository.findByUser(user);
    }

    public Expense updateExpense(Long id, ExpenseDTO expenseDTO, User user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only update your own expenses");
        }

        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(expenseDTO.getDate());
        expense.setDescription(expenseDTO.getDescription());
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    public void deleteExpense(Long id, User user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own expenses");
        }

        expenseRepository.delete(expense);
    }

    public Double getTotalExpenses(User user) {
        List<Expense> expenses = expenseRepository.findByUser(user);
        if (expenses == null || expenses.isEmpty()) {
            return 0.0;
        }
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public List<CategoryWiseExpense> getCategoryWiseExpenses(User user) {
        List<Object[]> results = expenseRepository.getCategoryWiseExpenses(user);
        return results.stream()
                .map(obj -> new CategoryWiseExpense((String) obj[0], (Double) obj[1]))
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByDateRange(User user, LocalDate start, LocalDate end) {
        return expenseRepository.findByUserAndDateBetween(user, start, end);
    }
}