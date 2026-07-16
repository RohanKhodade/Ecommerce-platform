package com.ecom.inventoryService.dto;

import com.ecom.inventoryService.dto.request.AddProductRequest;
import com.ecom.inventoryService.dto.response.ProductResponse;
import com.ecom.inventoryService.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Mapper {

    public ProductResponse toProductResponse(Product product){
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getId().toString());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity().toString());
        response.setCategoryName(product.getCategory().getName());
        response.setSellerId(product.getSellerId().toString());
        return response;
    }
    public Product toProduct(AddProductRequest request){
        Product product=new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice((new BigDecimal(request.getPrice())));
        product.setQuantity(request.getQuantity());
        return product;
    }

}
