package com.ecom.orderService.clients.inventoryService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlaceOrCancelRequest {
    private int quantity;
}
