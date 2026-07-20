package com.ecom.inventoryService.config;

import com.ecom.inventoryService.entity.Category;
import com.ecom.inventoryService.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            List<String> categories = List.of(
                    "Electronics",
                    "Clothing",
                    "Books",
                    "Home & Kitchen",
                    "Sports & Fitness",
                    "DIY Gadgets"
            );

            categories.forEach(name -> {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
            });

            System.out.println("Seeded " + categories.size() + " categories.");
        }
    }
}
