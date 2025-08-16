package org.example.remotly_ecommerce.helper.cart.search;

import org.example.remotly_ecommerce.model.Cart;
import org.example.remotly_ecommerce.model.User;

public interface CartSearchHelperInterface {
     Cart findUserCart(User user) ;
}
