package com.ecom.orderService.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long userId;
    private String orderStatus;
    private BigDecimal totalPrice;
    private String address;
    private String orderDate;
    private List<OrderItemResponse> orderItems=new ArrayList<>();
}
