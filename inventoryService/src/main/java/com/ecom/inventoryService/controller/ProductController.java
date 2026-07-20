package com.ecom.inventoryService.controller;

import com.ecom.inventoryService.dto.request.AddProductRequest;
import com.ecom.inventoryService.dto.request.OrderPlaceOrCancelRequest;
import com.ecom.inventoryService.dto.request.UpdateProductRequest;
import com.ecom.inventoryService.dto.response.ProductResponse;
import com.ecom.inventoryService.entity.Product;
import com.ecom.inventoryService.services.implementation.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class ProductController {

    private final ProductServiceImpl productService;
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId){
        return new ResponseEntity<> (productService.findById(productId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @GetMapping("/sellerProducts")
    public ResponseEntity<List<ProductResponse>> getAllProductsOfSeller(){
        return new ResponseEntity<>(productService.getAllProductsOfSeller(),HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody AddProductRequest request){
        return new ResponseEntity<> (productService.add(request),HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@Valid
                                                             @RequestBody UpdateProductRequest request,
                                                         @PathVariable Long productId,
                                                         @PathVariable Long sellerId){
        return new ResponseEntity<> (productService.update(request, productId),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId,
                                           @PathVariable Long sellerId){
        return new ResponseEntity<>
                (productService.delete(productId),HttpStatus.NO_CONTENT);
    }
    @PostMapping("/product/place/{productId}")
    public ResponseEntity<String> decrementInventory(@PathVariable Long productId,@RequestBody OrderPlaceOrCancelRequest request
                                                     ){
        return new ResponseEntity<> (productService.placeOrder(productId,request),HttpStatus.OK);
    }

    @PostMapping("/product/cancel/{productId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long productId,@RequestBody OrderPlaceOrCancelRequest request){
        return new ResponseEntity<> (productService.cancelOrder(productId,request),HttpStatus.OK);
    }
}