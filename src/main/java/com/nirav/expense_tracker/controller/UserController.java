package com.nirav.expense_tracker.controller;

import com.nirav.expense_tracker.dto.response.ApiResponse;
import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.exception.UnauthorizedAccessException;
import com.nirav.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable String username) {
        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User currentUser = userService.findByUsername(currentUsername);

        if ("ADMIN".equals(currentUser.getRole())) {
            User user = userService.findByUsername(username);
            return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
        }

        if (!currentUsername.equals(username)) {
            throw new UnauthorizedAccessException(
                    "You can only view your own profile. You are logged in as: " + currentUsername +
                            " but trying to view: " + username,
                    currentUsername
            );
        }

        User user = userService.findByUsername(username);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getProfile() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", user));
    }
}