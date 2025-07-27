package org.example.remotly_ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a single item in an order.
 * Each OrderItem is linked to a specific product and order,
 * and contains quantity, pricing, and size details.
 *
 * This entity is part of the order-items relationship with Order.
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
public class OrderItem {

    /**
     * Primary key - unique identifier for the order item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the parent order this item belongs to.
     * Ignored during JSON serialization to prevent circular references.
     */
    @JsonIgnore
    @ManyToOne
    private Order order;

    /**
     * The product associated with this order item.
     */
    @ManyToOne
    private Product product;

    /**
     * Size of the product ordered (e.g., S, M, L).
     */
    private String size;

    /**
     * Quantity of the product ordered.
     */
    private Integer quantity;

    /**
     * Original price (MRP) of the product.
     */
    private Double mrpPrice;

    /**
     * Final selling price of the product after any discounts.
     */
    private Double sellingPrice;

    /**
     * ID of the user who placed the order (duplicated for reference).
     */
    private Long userId;
}
