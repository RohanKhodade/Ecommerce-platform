package com.ecom.inventoryService.services.implementation;

import com.ecom.inventoryService.dto.Mapper;
import com.ecom.inventoryService.dto.request.AddProductRequest;
import com.ecom.inventoryService.dto.request.OrderPlaceOrCancelRequest;
import com.ecom.inventoryService.dto.request.UpdateProductRequest;
import com.ecom.inventoryService.dto.response.ProductResponse;
import com.ecom.inventoryService.entity.Category;
import com.ecom.inventoryService.entity.Product;
import com.ecom.inventoryService.exception.AccessDeniedException;
import com.ecom.inventoryService.exception.InsufficientStockException;
import com.ecom.inventoryService.exception.ResourceNotFoundException;
import com.ecom.inventoryService.repo.CategoryRepository;
import com.ecom.inventoryService.repo.ProductRepository;
import com.ecom.inventoryService.security.UserAccessGuard;
import com.ecom.inventoryService.services.service.ProductService;
import jakarta.transaction.Transactional;
import org.hibernate.query.Order;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Mapper mapper;
    private final CategoryRepository categoryRepository;
    private final UserAccessGuard userAccessGuard;
    private final OrderTransactions orderTransactions;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              UserAccessGuard userAccessGuard,
                              Mapper mapper,
                              OrderTransactions orderTransactions) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userAccessGuard = userAccessGuard;
        this.mapper = mapper;
        this.orderTransactions = orderTransactions;
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapper.toProductResponse(product);
    }

    @Override
    public String delete(Long productId, Long sellerId) {
        if (!userAccessGuard.isUserVerified(sellerId)) {
            throw new AccessDeniedException("Access denied,  " +
                    "you are not allowed to delete this product");
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + productId));
        productRepository.delete(product);
        return "Product Deleted SuccessFully";
    }

    @Override
    public ProductResponse update(UpdateProductRequest request,
                                  Long productId,
                                  Long sellerId) {
        if (!userAccessGuard.isUserVerified(sellerId)) {
            throw new AccessDeniedException("Access Denied" +
                    " you are not allowed to delete this product");
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + productId));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        return mapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse add(AddProductRequest request,
                               Long sellerId) {
        if (!userAccessGuard.isUserVerified(sellerId)){
            throw new AccessDeniedException("Access Denied"+
                    " you are not allowed to add this product");
        }
        Product product = mapper.toProduct(request);
        Category category = categoryRepository.findById(request.getCategoryId()).
                orElseThrow(() -> new ResourceNotFoundException("Category not found, id: " +
                        request.getCategoryId()));
        product.setCategory(category);
        product.setSellerId(sellerId);
        return mapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public List<ProductResponse> getAllProducts(){
        List<Product> products=productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for(Product product:products){
            productResponses.add(mapper.toProductResponse(product));
        }
        return productResponses;
    }
    @Override
    public List<ProductResponse> getAllProductsOfSeller(Long sellerId) {
        if (!userAccessGuard.isUserVerified(sellerId)){
            throw new AccessDeniedException("Access Denied" +
                    " you are not allowed to delete this product");
        }
        List<Product> products=productRepository.findBySellerId(sellerId);
        List<ProductResponse> productResponses = new ArrayList<>();
        for(Product product:products){
            productResponses.add(mapper.toProductResponse(product));
        }
        return productResponses;
    }

    @Override
    public String placeOrder(OrderPlaceOrCancelRequest request, Long productId){
        int MAX_ATTEMPTS=3;
        int attempts=0;
        while(attempts<MAX_ATTEMPTS){
            try{
                return orderTransactions.doPlaceOrder(request,productId);
            }catch(OptimisticLockingFailureException ex){
                attempts++;
                if (attempts >= MAX_ATTEMPTS){
                    throw new RuntimeException(
                            "Max attempts reached, Could not place order , please try again, ");
                }
            }
        }
        throw new RuntimeException("Unexpected error placing the order");
    }

    @Override
    public String cancelOrder(OrderPlaceOrCancelRequest request, Long productId){
        int MAX_ATTEMPTS=3;
        int attempts=0;
        while(attempts<MAX_ATTEMPTS){
            try{
                return orderTransactions.doCancelOrder(request,productId);
            }catch(OptimisticLockingFailureException ex){
                attempts++;
                if(attempts >= MAX_ATTEMPTS){
                    throw new RuntimeException(
                            "Couldn't cancel order, Max attempts reached, try again");
                }
            }
        }
        throw new RuntimeException("Unexpected error cancelling the order");
    }
}
