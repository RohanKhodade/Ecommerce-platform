package com.ecom.orderService.service.serviceImpl;

import com.ecom.orderService.clients.inventoryService.InventoryClient;
import com.ecom.orderService.clients.inventoryService.OrderPlaceOrCancelRequest;
import com.ecom.orderService.clients.inventoryService.ProductResponse;
import com.ecom.orderService.clients.userService.UserAddressResponse;
import com.ecom.orderService.clients.userService.UserClient;
import com.ecom.orderService.dto.Mapper;
import com.ecom.orderService.dto.response.CartItemResponse;
import com.ecom.orderService.dto.response.OrderResponse;
import com.ecom.orderService.entity.*;
import com.ecom.orderService.exceptions.AccessDeniedException;
import com.ecom.orderService.exceptions.ResourceNotFoundException;
import com.ecom.orderService.repository.OrderItemRepository;
import com.ecom.orderService.repository.OrderRepository;
import com.ecom.orderService.security.AuthUtil;
import com.ecom.orderService.service.services.CartService;
import com.ecom.orderService.service.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final AuthUtil authUtil;
    private final InventoryClient inventoryClient;
    private final Mapper mapper;
    private final UserClient userClient;
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            CartService cartService,
                            AuthUtil authUtil,
                            InventoryClient inventoryClient,
                            Mapper mapper,
                            UserClient userClient
                             ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.authUtil=authUtil;
        this.inventoryClient = inventoryClient;
        this.mapper=mapper;
        this.userClient=userClient;
    }

    @Transactional
    public String checkOut() {
        // this method is atomic(@transaction)
        // get user id . then find get the cart associated with that user
        // get cart items ,
        // fetch live price of each product from inventory service
        // and store it in order Item as snapshot;
        // fetch payment and notification services
        //then save orderItems and order in db
        Long userId = authUtil.getLoggedInUserId();
        List<CartItemResponse> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            throw new AccessDeniedException("Empty cart cannot be placed");
        }
        Order order = createOrder();
        BigDecimal totalPrice=new BigDecimal(0);

        List<CartItemResponse> reservedItems=new ArrayList<>(); // to know which items are placed

        try {
            for (CartItemResponse cartItem : cartItems) {

                ProductResponse product = inventoryClient.getProductResponse(cartItem.getProductId());

                OrderPlaceOrCancelRequest requestQuantity = new OrderPlaceOrCancelRequest(cartItem.getQuantity());
                inventoryClient.placeOrder(Long.parseLong(product.getProductId()),requestQuantity);
                reservedItems.add(cartItem);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductId(Long.parseLong(product.getProductId()));
                orderItem.setProductName(product.getName());
                orderItem.setSellerId(Long.parseLong(product.getSellerId()));
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPriceAtPurchase(product.getPrice());
                BigDecimal subTotal =
                        product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                totalPrice = totalPrice.add(subTotal);
                orderItem.setSubTotal(subTotal);
                orderItem.setStatus(ItemStatus.CONFIRMED);
                orderItemRepository.save(orderItem);
            }
            // to do payment service call to make payment
            // send notification to user via notification service
            order.setTotalPrice(totalPrice);
            orderRepository.save(order);
            cartService.clearCart();
            return "Order placed";
        }catch (Exception ex){
            for (CartItemResponse reservedItem: reservedItems){
                try{
                    OrderPlaceOrCancelRequest requestQuantity =
                            new OrderPlaceOrCancelRequest(reservedItem.getQuantity());
                    inventoryClient.cancelOrder(reservedItem.getProductId(),requestQuantity);
                }catch(Exception CompensationEx){
                    log.error("Rollback of placed product failed productId= {} and quantity= {}",
                            reservedItem.getProductId(),reservedItem.getQuantity(), CompensationEx);
                }
            }
            throw ex;
        }
    }

    public String cancelOrder(Long orderId){

        long userId = authUtil.getLoggedInUserId();
        Order order=orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Order not found"));
        if (!order.getUserId().equals(userId)){
            throw new AccessDeniedException("Access denied");
        }
        if (order.getOrderStatus()==OrderStatus.DELIVERED ||
                order.getOrderStatus()==OrderStatus.SHIPPED){
            throw new IllegalStateException(order.getOrderStatus()+" cannot be cancelled");
        }
        try{
            List<OrderItem> orderItems=order.getOrderItems();
            for (OrderItem orderItem: orderItems){
                OrderPlaceOrCancelRequest requestQuantity=
                        new OrderPlaceOrCancelRequest(orderItem.getQuantity());
                inventoryClient.cancelOrder(orderItem.getProductId(),requestQuantity);
                orderItem.setStatus(ItemStatus.CANCELLED);
                orderItemRepository.save(orderItem);
            }
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return "Order cancelled";
        }catch (Exception ex){
            return ex.getMessage();
        }
    }

    public Order createOrder(){
        Order order=new Order();
        Long userId=authUtil.getLoggedInUserId();
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.PENDING);
        List<UserAddressResponse> addressList;
        try{
            addressList=userClient.getUserAddress(userId);
        }catch(Exception ex){
            throw ex;
        }
        UserAddressResponse firstAddress;
        firstAddress=addressList.getFirst();
        String address=firstAddress.getBuilding()+" "+firstAddress.getStreet()+
                " "+firstAddress.getCity()+" "+firstAddress.getState()+" "+firstAddress.getZip()
                +" "+firstAddress.getCountry();
        order.setAddress(address);
        return orderRepository.save(order);
    }
    public OrderResponse viewOrder(Long orderId){
        Order order=orderRepository.findById(orderId).orElseThrow(
                ()->  new ResourceNotFoundException("Order not found"));
        if (!order.getUserId().equals(authUtil.getLoggedInUserId())){
            throw new AccessDeniedException("Access denied");
        }
        return mapper.toOrderResponse(order);
    }

}
