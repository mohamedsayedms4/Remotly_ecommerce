package org.example.remotly_ecommerce.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.remotly_ecommerce.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer order in the system.
 * Each order is linked to a user and contains multiple order items,
 * shipping details, payment information, and delivery metadata.
 *
 * This entity is mapped to the "orders" table in the database.
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
@Table(name = "orders")
public class Order {

    /**
     * Primary key - unique identifier for the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Business-generated order identifier (can be UUID or custom format).
     */
    private String orderId;

    /**
     * The user who placed the order.
     */
    @ManyToOne
    private User user;

    /**
     * ID of the seller who is responsible for fulfilling the order.
     */
    private Long sellerId;

    /**
     * List of items associated with this order.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Shipping address where the order will be delivered.
     */
    @ManyToOne
    private Address shippingAddress;

    /**
     * Embedded payment details including method and status.
     */
    @Embedded
    private PaymentDetails paymentDetails = new PaymentDetails();

    /**
     * Total MRP (original) price before discounts.
     */
    private double totalMrpPrice;

    /**
     * Total price after applying discounts.
     */
    private double totalSellingPrice;

    /**
     * Discount percentage or value applied to the order.
     */
    private Integer discount;

    /**
     * Current status of the order (e.g., PENDING, SHIPPED, DELIVERED).
     */
    private OrderStatus orderStatus;

    /**
     * Total number of items in the order.
     */
    private Integer totalItems;

    /**
     * Date and time when the order was placed.
     */
    private LocalDateTime orderDate = LocalDateTime.now();

    /**
     * Estimated delivery date for the order (default is 7 days from order).
     */
    private LocalDateTime deliverDate = orderDate.plusDays(7);
}
