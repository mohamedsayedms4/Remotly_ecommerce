package org.example.remotly_ecommerce.repository;

import org.example.remotly_ecommerce.model.Cart;
import org.example.remotly_ecommerce.model.CartItem;
import org.example.remotly_ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size) ;
}
