package org.example.remotly_ecommerce.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a wishlist entity associated with a user.
 * A wishlist contains a collection of products the user is interested in.
 *
 * Useful in e-commerce systems for saving products for later viewing or purchasing.
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
public class Wishlist {

    /**
     * The unique identifier for the wishlist.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who owns the wishlist.
     */
    @OneToOne
    private User user;

    /**
     * A set of products saved in the wishlist.
     */
    @ManyToMany
    private Set<Product> products = new HashSet<>();
}
