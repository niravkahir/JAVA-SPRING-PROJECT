package com.nirav.expense_tracker.controller;

import com.nirav.expense_tracker.dto.CategoryWiseExpense;
import com.nirav.expense_tracker.dto.ExpenseDTO;
import com.nirav.expense_tracker.dto.response.ApiResponse;
import com.nirav.expense_tracker.entity.Expense;
import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.service.ExpenseService;
import com.nirav.expense_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResponse<Expense>> addExpense(@Valid @RequestBody ExpenseDTO expenseDTO) {
        User currentUser = getCurrentUser();
        Expense expense = expenseService.addExpense(expenseDTO, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Expense added successfully", expense));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Expense>>> getUserExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        User currentUser = getCurrentUser();
        Page<Expense> expenses = expenseService.getUserExpensesPaginated(currentUser, page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success("Expenses retrieved successfully", expenses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseDTO expenseDTO) {
        User currentUser = getCurrentUser();
        Expense expense = expenseService.updateExpense(id, expenseDTO, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Expense updated successfully", expense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        expenseService.deleteExpense(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Expense deleted successfully"));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<Double>> getTotalExpenses() {
        User currentUser = getCurrentUser();
        Double total = expenseService.getTotalExpenses(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Total expenses retrieved successfully", total));
    }

    @GetMapping("/category-wise")
    public ResponseEntity<ApiResponse<List<CategoryWiseExpense>>> getCategoryWiseExpenses() {
        User currentUser = getCurrentUser();
        List<CategoryWiseExpense> expenses = expenseService.getCategoryWiseExpenses(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Category-wise expenses retrieved successfully", expenses));
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<Expense>>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        User currentUser = getCurrentUser();
        List<Expense> expenses = expenseService.getExpensesByDateRange(currentUser, start, end);
        return ResponseEntity.ok(ApiResponse.success("Expenses retrieved successfully", expenses));
    }

    @GetMapping("/date-range/paginated")
    public ResponseEntity<ApiResponse<Page<Expense>>> getExpensesByDateRangePaginated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        User currentUser = getCurrentUser();
        Page<Expense> expenses = expenseService.getExpensesByDateRangePaginated(
                currentUser, start, end, page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success("Expenses retrieved successfully", expenses));
    }
}