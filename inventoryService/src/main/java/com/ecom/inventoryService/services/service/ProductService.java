package com.ecom.inventoryService.services.service;

import com.ecom.inventoryService.dto.request.AddProductRequest;
import com.ecom.inventoryService.dto.request.UpdateProductRequest;
import com.ecom.inventoryService.dto.response.ProductResponse;
import com.ecom.inventoryService.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponse findById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getAllProductsOfSeller(Long sellerId);
    String delete(Long productId,Long sellerId);
    ProductResponse update(UpdateProductRequest request, Long productId, Long sellerId);

    ProductResponse add(AddProductRequest request, Long sellerId);
}
