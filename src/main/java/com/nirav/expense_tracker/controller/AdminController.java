package com.nirav.expense_tracker.controller;

import com.nirav.expense_tracker.dto.response.ApiResponse;
import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.exception.ResourceNotFoundException;
import com.nirav.expense_tracker.repository.UserRepository;
import com.nirav.expense_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } else {
            throw new ResourceNotFoundException("User", "id", id);
        }
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<User>> updateUserRole(
            @PathVariable Long id,
            @RequestParam String role) {
        User updatedUser = userService.updateUserRole(id, role);
        return ResponseEntity.ok(ApiResponse.success("User role updated successfully", updatedUser));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User userDetails) {
        User existingUser = userService.findById(id);

        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());

        User updatedUser = userService.updateUser(existingUser);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }
}