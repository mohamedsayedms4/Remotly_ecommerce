package org.example.remotly_ecommerce.helper.cart.creation;

import org.example.remotly_ecommerce.model.CartItem;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.User;

public interface CartCreationHelperInterface {
    CartItem addProductToCart(Product product , User user , String size , int quantity) ;

}
