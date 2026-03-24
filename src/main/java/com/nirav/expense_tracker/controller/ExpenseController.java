package com.nirav.expense_tracker.controller;

import com.nirav.expense_tracker.dto.CategoryWiseExpense;
import com.nirav.expense_tracker.dto.ExpenseDTO;
import com.nirav.expense_tracker.entity.Expense;
import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.service.ExpenseService;
import com.nirav.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username);
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseDTO expenseDTO) {
        User currentUser = getCurrentUser();
        Expense expense = expenseService.addExpense(expenseDTO, currentUser);
        return ResponseEntity.ok(expense);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getUserExpenses() {
        User currentUser = getCurrentUser();
        List<Expense> expenses = expenseService.getUserExpenses(currentUser);
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody ExpenseDTO expenseDTO) {
        User currentUser = getCurrentUser();
        Expense expense = expenseService.updateExpense(id, expenseDTO, currentUser);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        expenseService.deleteExpense(id, currentUser);
        return ResponseEntity.ok("Expense deleted successfully");
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalExpenses() {
        User currentUser = getCurrentUser();
        Double total = expenseService.getTotalExpenses(currentUser);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/category-wise")
    public ResponseEntity<List<CategoryWiseExpense>> getCategoryWiseExpenses() {
        User currentUser = getCurrentUser();
        List<CategoryWiseExpense> expenses = expenseService.getCategoryWiseExpenses(currentUser);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        User currentUser = getCurrentUser();
        List<Expense> expenses = expenseService.getExpensesByDateRange(currentUser, start, end);
        return ResponseEntity.ok(expenses);
    }
}