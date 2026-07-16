package com.ecom.orderService.service.serviceImpl;

import com.ecom.orderService.dto.Mapper;
import com.ecom.orderService.dto.requests.CartItemRequest;
import com.ecom.orderService.dto.response.CartItemResponse;
import com.ecom.orderService.entity.Cart;
import com.ecom.orderService.entity.CartItem;
import com.ecom.orderService.exceptions.AccessDeniedException;
import com.ecom.orderService.exceptions.ResourceNotFoundException;
import com.ecom.orderService.repository.CartItemRepository;
import com.ecom.orderService.repository.CartRepository;
import com.ecom.orderService.security.AuthUtil;
import com.ecom.orderService.service.services.CartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthUtil authUtil;
    private final Mapper mapper;
    public CartServiceImpl(CartRepository cartRepository,
                           AuthUtil authUtil,
                           CartItemRepository cartItemRepository,
                           Mapper mapper) {
        this.cartRepository=cartRepository;
        this.authUtil=authUtil;
        this.cartItemRepository=cartItemRepository;
        this.mapper=mapper;
    }

    public Cart getCart(){
        Long userId=authUtil.getLoggedInUserId();
        return cartRepository.getByUserId(userId).orElseThrow(
                ()->new ResourceNotFoundException("Cart not found")
        );
    }
    public String addItemToCart(CartItemRequest request){
        Long userId=authUtil.getLoggedInUserId();
        Cart cart=cartRepository.getByUserId(userId).orElse(null);
        CartItem cartItem=new CartItem();
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        if (cart!=null){
            cartItem.setCart(cart);
        }else{
            Cart newCart=new Cart();
            newCart.setUserId(userId);
            Cart savedCart=cartRepository.save(newCart);
            cartItem.setCart(savedCart);
        }
        cartItemRepository.save(cartItem);
        return "Item Added to cart";
    }
    public String removeItemFromCart(Long itemId){
        Long userId=authUtil.getLoggedInUserId();
        getCart();
        CartItem cartItem=cartItemRepository.findById(itemId).orElseThrow(
                ()->new ResourceNotFoundException("Item not found")
        );
        if (!cartItem.getCart().getUserId().equals(userId)){
            throw new AccessDeniedException("Access denied");
        }
        cartItemRepository.delete(cartItem);
        return "Item Removed from cart";    }

    public List<CartItemResponse> getCartItems(){
        Cart cart=getCart();
        List<CartItem> itemList=cart.getCartItems();
        List<CartItemResponse> cartItemsResponseList=new ArrayList<>();
        for (CartItem item:itemList){
            cartItemsResponseList.add(mapper.toCartItemResponse(item));
        }
        return cartItemsResponseList;
    }

    public CartItemResponse getCartItem(Long itemId){
        CartItem cartItem=cartItemRepository.findById(itemId).orElseThrow(
                ()-> new ResourceNotFoundException("Item not found")
        );
        Long userId=authUtil.getLoggedInUserId();
        if (!userId.equals(cartItem.getCart().getUserId())){
            throw new AccessDeniedException("Access denied ");
        }
        return mapper.toCartItemResponse(cartItem);
    }

    public String updateCartItemQuantity(CartItemRequest request){
        Long userId=authUtil.getLoggedInUserId();
        Cart cart=getCart();
        CartItem cartItem=cartItemRepository.findById(request.getProductId()).orElseThrow(
                ()->new ResourceNotFoundException("Item not found")
        );
        if(!cartItem.getCart().getUserId().equals(userId)){
           throw new AccessDeniedException("Access denied");
        }
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
        return "Item Updated";
    }

    public String clearCart(){
        Long userId=authUtil.getLoggedInUserId();
        Cart existingCart=getCart();
        Cart cart=cartRepository.getByUserId(userId).orElseThrow(
                ()-> new ResourceNotFoundException("Cart not found")
        );
        cartItemRepository.deleteAll(cart.getCartItems());
        cartRepository.save(existingCart);
        return "Cart cleared ";
    }
}
