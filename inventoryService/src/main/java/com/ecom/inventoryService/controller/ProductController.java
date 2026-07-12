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

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductResponse>> getAllProductsOfSeller(@PathVariable Long sellerId){
        return new ResponseEntity<>(productService.getAllProductsOfSeller(sellerId),HttpStatus.OK);
    }


    @PostMapping("/add/{sellerId}")
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody AddProductRequest request,
                                                      @PathVariable Long sellerId){
        return new ResponseEntity<> (productService.add(request,sellerId),HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}/{sellerId}")
    public ResponseEntity<ProductResponse> updateProduct(@Valid
                                                             @RequestBody UpdateProductRequest request,
                                                         @PathVariable Long productId,
                                                         @PathVariable Long sellerId){
        return new ResponseEntity<> (productService.update(request, productId, sellerId),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{productId}/{sellerId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId,
                                           @PathVariable Long sellerId){
        return new ResponseEntity<>
                (productService.delete(productId,sellerId),HttpStatus.NO_CONTENT);
    }
    @PostMapping("/order/place/{productId}")
    public ResponseEntity<String> decrementInventory(@RequestBody OrderPlaceOrCancelRequest request,
                                                     @PathVariable Long productId){
        return new ResponseEntity<> (productService.placeOrder(request,productId),HttpStatus.OK);
    }

    @PostMapping("/order/cancel/{productId}")
    public ResponseEntity<String> cancelOrder(@RequestBody OrderPlaceOrCancelRequest request,
                                              @PathVariable Long productId){
        return new ResponseEntity<> (productService.cancelOrder(request,productId),HttpStatus.OK);
    }
}