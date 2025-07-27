package org.example.remotly_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a shopping cart associated with a specific user.
 * It holds items the user wants to purchase and pricing details.
 *
 * Each cart contains a set of {@link CartItem} objects, tracks total prices,
 * and optionally applies discount or coupon codes.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Cart {

    /**
     * Unique identifier for the cart.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who owns the cart.
     */
    @OneToOne
    private User user;

    /**
     * The set of items contained in the cart.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    /**
     * The total price of all items based on their selling prices.
     */
    private Double totalSellingPrice;

    /**
     * The total quantity of items in the cart.
     */
    private Integer quantity;

    /**
     * The total MRP (Maximum Retail Price) of all items in the cart.
     */
    private Integer totalMrpPrice;

    /**
     * The discount applied to the cart.
     */
    private Integer discount;

    /**
     * The coupon code used, if any.
     */
    private String couponCode;
}
