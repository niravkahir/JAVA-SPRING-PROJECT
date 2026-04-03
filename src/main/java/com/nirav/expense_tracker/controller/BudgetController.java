package com.nirav.expense_tracker.controller;

import com.nirav.expense_tracker.entity.Budget;
import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.service.BudgetService;
import com.nirav.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Budget setBudget(@RequestParam Double monthlyLimit) {
        User currentUser = getCurrentUser();
        return budgetService.setBudget(monthlyLimit, currentUser);
    }

    @GetMapping
    public Budget getBudget() {
        User currentUser = getCurrentUser();
        return budgetService.getUserBudget(currentUser);
    }

    @GetMapping("/status")
    public String getBudgetStatus() {
        User currentUser = getCurrentUser();
        return budgetService.checkBudgetStatus(currentUser);
    }
}