package com.nirav.expense_tracker.dto.response;

public class JwtResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private String name;
    private String email;
    private String role;

    public JwtResponse(String token, String username, String name, String email, String role) {
        this.token = token;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setToken(String token) {
        this.token = token;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}