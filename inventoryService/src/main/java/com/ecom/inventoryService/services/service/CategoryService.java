package com.ecom.inventoryService.services.service;


import com.ecom.inventoryService.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
}