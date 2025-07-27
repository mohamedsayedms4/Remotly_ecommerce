package org.example.remotly_ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing an individual item within a shopping cart.
 * Each item is linked to a specific {@link Product} and {@link Cart},
 * and contains pricing and quantity information.
 *
 * This class supports multiple items in a cart, each potentially differing in size or quantity.
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
public class CartItem {

    /**
     * Unique identifier for the cart item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The cart that this item belongs to.
     * Marked as @JsonIgnore to avoid recursive serialization.
     */
    @ManyToOne
    @JsonIgnore
    private Cart cart;

    /**
     * Size of the product (if applicable).
     */
    private String size;

    /**
     * Quantity of the product in the cart. Default is 1.
     */
    private Integer quantity = 1;

    /**
     * Maximum Retail Price (MRP) of the product.
     */
    private Integer mrpPrice;

    /**
     * Selling price of the product (after discounts).
     */
    private Integer sellingPrice;

    /**
     * ID of the user who added this item to the cart.
     */
    private Long userId;

    /**
     * The actual product linked to this cart item.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
