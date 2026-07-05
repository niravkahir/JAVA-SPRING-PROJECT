// Purpose: Handle permission issues
// Why: Currently throwing RuntimeException in UserController
// Used when: User tries to access others' data

package com.nirav.expense_tracker.exception;

public class UnauthorizedAccessException extends RuntimeException {

    private String message;
    private String username;

    public UnauthorizedAccessException(String message) {
        super(message);
        this.message = message;
    }

    public UnauthorizedAccessException(String message, String username) {
        super(String.format("User '%s' is not authorized: %s", username, message));
        this.message = message;
        this.username = username;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }
}