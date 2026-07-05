package com.nirav.expense_tracker.controller;

import com.nirav.expense_tracker.dto.response.ApiResponse;
import com.nirav.expense_tracker.entity.Budget;
import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.service.BudgetService;
import com.nirav.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username);
    }

    @PostMapping("/set")
    public ResponseEntity<ApiResponse<Budget>> setBudget(@RequestParam Double monthlyLimit) {
        if (monthlyLimit == null || monthlyLimit <= 0) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Monthly limit must be greater than 0"));
        }

        User currentUser = getCurrentUser();
        Budget budget = budgetService.setBudget(monthlyLimit, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Budget set successfully", budget));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Budget>> getBudget() {
        User currentUser = getCurrentUser();
        Budget budget = budgetService.getUserBudget(currentUser);
        if (budget == null) {
            return ResponseEntity.ok(ApiResponse.success("No budget set for this month", null));
        }
        return ResponseEntity.ok(ApiResponse.success("Budget retrieved successfully", budget));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> getBudgetStatus() {
        User currentUser = getCurrentUser();
        String status = budgetService.checkBudgetStatus(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Budget status retrieved successfully", status));
    }
}