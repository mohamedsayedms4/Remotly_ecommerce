package org.example.remotly_ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    private Double totalSellingPrice;
    private Integer quantity;
    private Integer totalMrpPrice;
    private Integer discount;
    private String couponCode;
}
