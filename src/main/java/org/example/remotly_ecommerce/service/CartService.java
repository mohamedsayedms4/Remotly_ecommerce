package org.example.remotly_ecommerce.service;

import org.example.remotly_ecommerce.exception.ProductException;
import org.example.remotly_ecommerce.model.Cart;
import org.example.remotly_ecommerce.model.CartItem;
import org.example.remotly_ecommerce.model.User;

public interface CartService {

    CartItem addProductToCart(CartItem cartItem ) ;
    Cart findUserCart(User user);

}
