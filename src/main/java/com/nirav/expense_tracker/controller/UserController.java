package com.nirav.expense_tracker.controller;

import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User currentUser = userService.findByUsername(currentUsername);

        if ("ADMIN".equals(currentUser.getRole())) {
            return userService.findByUsername(username);
        }

        if (!currentUsername.equals(username)) {
            throw new RuntimeException("You can only view your own profile. " +
                    "You are logged in as: " + currentUsername +
                    " but trying to view: " + username);
        }

        return userService.findByUsername(username);
    }

    @GetMapping("/profile")
    public User getProfile() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userService.findByUsername(username);
    }
}