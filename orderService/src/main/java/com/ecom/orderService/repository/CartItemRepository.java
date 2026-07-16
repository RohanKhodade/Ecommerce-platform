package com.ecom.orderService.repository;

import com.ecom.orderService.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> getByCartId(Long cartId);
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
