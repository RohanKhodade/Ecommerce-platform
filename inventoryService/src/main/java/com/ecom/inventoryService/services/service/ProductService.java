package com.ecom.inventoryService.services.service;

import com.ecom.inventoryService.dto.request.AddProductRequest;
import com.ecom.inventoryService.dto.request.OrderPlaceOrCancelRequest;
import com.ecom.inventoryService.dto.request.UpdateProductRequest;
import com.ecom.inventoryService.dto.response.ProductResponse;
import com.ecom.inventoryService.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponse findById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getAllProductsOfSeller();
    String delete(Long productId);
    ProductResponse update(UpdateProductRequest request, Long productId);

    ProductResponse add(AddProductRequest request);
    String placeOrder( Long productId,OrderPlaceOrCancelRequest request);
    String cancelOrder( Long productId,OrderPlaceOrCancelRequest request);
}
