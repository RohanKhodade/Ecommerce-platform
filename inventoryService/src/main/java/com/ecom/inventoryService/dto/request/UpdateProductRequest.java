package com.ecom.inventoryService.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {

    @NotBlank(message="Product name is required")
    @Size(min=3,max=100,message="name must be within 3 to 100 characters")
    private String name;

    @NotBlank(message="Product name is required")
    @Size(min=3,max=100,message="description must be within 3 to 100 characters")
    private String description;

    @NotNull(message="updated price is required")
    @Min(value=0,message="price cannot be zero")
    private BigDecimal price;

    @NotNull(message="updated quantity is required")
    @Min(value=0,message="quantity cannot be zero")
    private int quantity;
}
