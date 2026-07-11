package com.ecom.inventoryService.dto.response;

import lombok.Data;

@Data
public class ProductResponse {

    private String productId;
    private String name;
    private String description;
    private String quantity;
    private String price;
    private String categoryName;
}
