package org.example.remotly_ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.example.remotly_ecommerce.domain.UserRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user in the e-commerce system.
 * A user can have multiple addresses and may use multiple coupons.
 * Users are assigned roles such as CUSTOMER or ADMIN.
 *
 * This class is mapped to the "users" table in the database.
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
@Table(name = "users")
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user's password. This field is write-only during serialization.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The full name of the user.
     */
    private String fullName;

    /**
     * The user's phone number.
     */
    private String phoneNumber;

    /**
     * The role of the user (e.g., CUSTOMER, ADMIN, etc.).
     */
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_CUSTOMER;

    /**
     * A set of addresses associated with the user.
     */
    @OneToMany
    private Set<Address> addresses = new HashSet<>();

    /**
     * A set of coupons the user has used. This field is ignored during serialization.
     */
    @ManyToMany
    @JsonIgnore
    private Set<Coupon> usedCoupons = new HashSet<>();
}
