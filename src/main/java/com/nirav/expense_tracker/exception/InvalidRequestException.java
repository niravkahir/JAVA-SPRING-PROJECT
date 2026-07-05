package com.nirav.expense_tracker.exception;

import java.util.Map;

public class InvalidRequestException extends RuntimeException {

    private Map<String, String> validationErrors;

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Map<String, String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}