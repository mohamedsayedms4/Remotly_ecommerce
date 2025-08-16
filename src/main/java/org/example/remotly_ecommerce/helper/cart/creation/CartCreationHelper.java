package org.example.remotly_ecommerce.helper.cart.creation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.helper.cart.search.CartSearchHelperInterface;
import org.example.remotly_ecommerce.model.Cart;
import org.example.remotly_ecommerce.model.CartItem;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.CartItemRepository;
import org.example.remotly_ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartCreationHelper implements CartCreationHelperInterface{
    private final CartRepository cartRepository;
    private final CartItemRepository  cartItemRepository;
    private final CartSearchHelperInterface cartSearchHelper;

    @Override
    public CartItem addProductToCart(Product product , User user , String size , int quantity) {
        Cart cart = cartSearchHelper.findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(
                cart , product , size
        );
        if(isPresent == null){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setSize(size);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            double totalPrice = quantity* product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return  cartItemRepository.save(cartItem);
        }

        return isPresent
                ;
    }
}
