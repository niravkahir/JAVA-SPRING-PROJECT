package com.nirav.expense_tracker.dto;

import java.time.LocalDate;

public class ExpenseDTO {
    private Double amount;
    private LocalDate date;
    private String description;
    private Long categoryId;

    // Getters
    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    // Setters
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}