package com.nirav.expense_tracker.service;

import com.nirav.expense_tracker.entity.Category;
import com.nirav.expense_tracker.exception.DuplicateResourceException;
import com.nirav.expense_tracker.exception.ResourceNotFoundException;
import com.nirav.expense_tracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category) {
        // Check if category with same name exists
        List<Category> existingCategories = categoryRepository.findAll();
        for (Category existing : existingCategories) {
            if (existing.getName().equalsIgnoreCase(category.getName())) {
                throw new DuplicateResourceException("Category", "name", category.getName());
            }
        }
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}