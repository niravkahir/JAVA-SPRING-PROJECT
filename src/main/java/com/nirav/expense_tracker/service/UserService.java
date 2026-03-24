package com.nirav.expense_tracker.service;

import com.nirav.expense_tracker.dto.RegisterRequest;
import com.nirav.expense_tracker.entity.User;
import com.nirav.expense_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String role = request.getRole();
        if (role != null && !role.isEmpty()) {
            if (!role.equals("USER") && !role.equals("ADMIN")) {
                throw new RuntimeException("Invalid role! Role must be 'USER' or 'ADMIN'");
            }
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role != null && !role.isEmpty() ? role : "USER");

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    public User updateUserRole(Long userId, String role) {
        User user = findById(userId);

        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new RuntimeException("Invalid role. Must be USER or ADMIN");
        }

        user.setRole(role);
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}