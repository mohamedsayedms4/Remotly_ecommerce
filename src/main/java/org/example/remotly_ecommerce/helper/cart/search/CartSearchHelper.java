package org.example.remotly_ecommerce.helper.cart.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.model.Cart;
import org.example.remotly_ecommerce.model.CartItem;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.CartItemRepository;
import org.example.remotly_ecommerce.repository.CartRepository;
import org.example.remotly_ecommerce.utilis.CalculateDiscountPercentage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartSearchHelper implements CartSearchHelperInterface {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    /**
     * @param user
     * @return
     */
    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        Double totalPrice = 0.0;
        Double totalDiscountPrice = 0.0;

        Integer totalItems = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice +=  cartItem.getMrpPrice();
            totalDiscountPrice += cartItem.getSellingPrice();
            totalItems += cartItem.getQuantity();
        }
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalSellingPrice(totalDiscountPrice);

        cart.setDiscount(CalculateDiscountPercentage.calculate(totalPrice, totalDiscountPrice));
        cart.setCartItems(cart.getCartItems());
        return null;
    }

}
