package com.ecom.orderService.clients.inventoryService;

import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="inventoryService")
public interface InventoryClient {

    @GetMapping("/api/inventory/all")
    ProductResponse getAllProducts();

    @GetMapping("/api/inventory/{productId}")
    ProductResponse getProductResponse(@PathVariable Long productId);

    @PostMapping("/api/inventory/product/place/{productId}")
    String placeOrder(@PathVariable Long productId,
                      @RequestBody OrderPlaceOrCancelRequest requestQuantity);

    @PostMapping("/api/inventory/product/cancel/{productId}")
    String cancelOrder(@PathVariable Long productId,
                       @RequestBody OrderPlaceOrCancelRequest requestQuantity);
}
