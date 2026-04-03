package com.nirav.expense_tracker.dto;

public class CategoryWiseExpense {
    private String categoryName;
    private Double totalAmount;

    // Constructor
    public CategoryWiseExpense(String categoryName, Double totalAmount) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getCategoryName() {
        return categoryName;
    }
    public Double getTotalAmount() {
        return totalAmount;
    }

    // Setters
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}