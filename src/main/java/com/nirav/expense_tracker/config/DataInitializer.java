package com.nirav.expense_tracker.config;

import com.nirav.expense_tracker.entity.Category;
import com.nirav.expense_tracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            Category[] categories = {
                    new Category("Food", "Food and dining expenses"),
                    new Category("Travel", "Transportation and travel expenses"),
                    new Category("Shopping", "Shopping and retail expenses"),
                    new Category("Entertainment", "Movies, games, and entertainment"),
                    new Category("Bills", "Utilities and bills"),
                    new Category("Healthcare", "Medical and health expenses"),
                    new Category("Education", "Education and learning expenses"),
                    new Category("Other", "Other miscellaneous expenses")
            };

            for (Category category : categories) {
                categoryRepository.save(category);
            }

            System.out.println("Default categories created successfully!");
        }
    }
}