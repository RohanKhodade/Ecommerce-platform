package com.ecom.inventoryService.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderPlaceOrCancelRequest {

    @NotNull(message="quantity to update cannot be null")
    @Min(value=1,message="quantity cannot be zero")
    private int quantity;
}