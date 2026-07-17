package com.ecom.orderService.dto;

import com.ecom.orderService.dto.response.CartItemResponse;
import com.ecom.orderService.dto.response.OrderItemResponse;
import com.ecom.orderService.dto.response.OrderResponse;
import com.ecom.orderService.entity.CartItem;
import com.ecom.orderService.entity.Order;
import com.ecom.orderService.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class Mapper {



    public CartItemResponse toCartItemResponse(CartItem cartItem){
        CartItemResponse cartItemResponse= new CartItemResponse();
        cartItemResponse.setProductId(cartItem.getProductId());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setCartId(cartItem.getId());
        return cartItemResponse;
    }

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem){
        OrderItemResponse response=new OrderItemResponse();
        response.setProductId(orderItem.getProductId());
        response.setProductName(orderItem.getProductName());
        response.setPriceAtPurchase(orderItem.getPriceAtPurchase());
        response.setQuantity(orderItem.getQuantity());
        response.setSubtotal(orderItem.getSubTotal());
        response.setOrderId(orderItem.getId());
        response.setSellerId(orderItem.getSellerId());
        response.setItemStatus(orderItem.getStatus().toString());
        return response;
    }

    public OrderResponse toOrderResponse(Order order){
        OrderResponse response=new OrderResponse();
        response.setOrderId(order.getId());
        response.setOrderDate(order.getOrderDate().toString());
        response.setOrderStatus(order.getOrderStatus().toString());
        response.setAddress(order.getAddress());
        response.setUserId(order.getUserId());
        response.setTotalPrice(order.getTotalPrice());
        List<OrderItem> orderItems=order.getOrderItems();
        List<OrderItemResponse> orderItemsResponseList=new ArrayList<>();
        for (OrderItem item:orderItems){
            OrderItemResponse orderItemResponse=new OrderItemResponse();
            orderItemsResponseList.add(toOrderItemResponse(item));
        }
        response.setOrderItems(orderItemsResponseList);
        return response;
    }
}
