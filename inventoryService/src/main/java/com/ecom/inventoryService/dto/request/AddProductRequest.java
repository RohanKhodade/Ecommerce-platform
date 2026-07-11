package com.ecom.inventoryService.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class AddProductRequest {

    @NotBlank(message="Product name is required")
    @Size(min=3,max=100,message="name must be within 3 to 100 characters")
    private String name;

    @NotBlank(message="Product description name is required")
    @Size(min=3,max=100,message="description must be within 3 to 100 characters")
    private String description;

    @NotNull(message="Product price is required")
    private double price;

    @NotNull(message="Product quantity is required")
    @Min(value=0,message="quantity cannot be negative")
    private int quantity;

    @NotNull(message="Product category is required")
    @Positive(message = "Category ID must be a positive number")
    private long categoryId;
}