package com.ecom.inventoryService.services.implementation;

import com.ecom.inventoryService.dto.request.OrderPlaceOrCancelRequest;
import com.ecom.inventoryService.entity.Product;
import com.ecom.inventoryService.exception.InsufficientStockException;
import com.ecom.inventoryService.exception.ResourceNotFoundException;
import com.ecom.inventoryService.repo.ProductRepository;
import jakarta.transaction.Transactional;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderTransactions {

    private final ProductRepository productRepository;
    public OrderTransactions(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public String doPlaceOrder( Long productId,OrderPlaceOrCancelRequest request){
        Product product=productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product not found with id: " + productId));
        int quantity=product.getQuantity()-request.getQuantity();
        if (quantity<0){
            throw new InsufficientStockException("Ordered Quantity is too high !"+
                    " remaining quantity: "+product.getQuantity());
        }
        product.setQuantity(quantity);
        productRepository.save(product);
        return "Order Placed Successfully";
    }

    @Transactional
    public String doCancelOrder( Long productId,OrderPlaceOrCancelRequest request){
        Product product=productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product not found with id: " + productId));
        int quantity=product.getQuantity()+request.getQuantity();
        product.setQuantity(quantity);
        productRepository.save(product);
        return "Order Cancelled Successfully";
    }
}
