package org.example.remotly_ecommerce.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

/**
 * Entity representing a discount coupon that can be applied to orders.
 * Each coupon has a code, discount percentage, validity period, and
 * conditions like minimum order value.
 *
 * A coupon can be associated with multiple users through a many-to-many relationship.
 *
 * Example: A coupon "SAVE10" giving 10% off on orders above 200 EGP.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Entity
public class Coupon {

    /**
     * Primary key of the coupon.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique coupon code that users apply at checkout.
     */
    private String code;

    /**
     * Discount percentage offered by the coupon (e.g., 10.0 for 10%).
     */
    private Double discountPercentage;

    /**
     * The start date from which the coupon becomes valid.
     */
    private LocalDate startDate;

    /**
     * The minimum order value required to apply this coupon.
     */
    private Double minimumOrderValue;

    /**
     * Indicates if the coupon is currently active and usable.
     */
    private Boolean isActive = true;

    /**
     * Users who have used this coupon.
     * Many-to-many relationship with the User entity.
     */
    @ManyToMany(mappedBy = "usedCoupons")
    private Set<User> users;
}
