package com.ecom.inventoryService.services.implementation;

import com.ecom.inventoryService.dto.response.CategoryResponse;
import com.ecom.inventoryService.entity.Category;
import com.ecom.inventoryService.exception.ResourceNotFoundException;
import com.ecom.inventoryService.repo.CategoryRepository;
import com.ecom.inventoryService.services.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse findById(Long categoryId){
        Category category=categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category not found with id: " + categoryId));
        CategoryResponse response = new CategoryResponse();
        response.setCategoryId(category.getId());
        response.setCategoryName(category.getName());
        return response;
    }

    public List<CategoryResponse> getAllCategories(){
        List<Category> categories=categoryRepository.findAll();
        List<CategoryResponse> categoriesResponse=new ArrayList<>();
        for (Category category: categories){
            CategoryResponse response=new CategoryResponse();
            response.setCategoryId(category.getId());
            response.setCategoryName(category.getName());
            categoriesResponse.add(response);
        }
        return categoriesResponse;
    }
}
