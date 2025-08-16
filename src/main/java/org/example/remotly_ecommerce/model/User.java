package org.example.remotly_ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;
    private String fullName;
    private String phoneNumber;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_coupons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    @JsonIgnore
    private Set<Coupon> usedCoupons = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress = new Address();

    private String imageUrl;
    // هذا الحقل الجديد للعلاقة مع Cart:
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Cart cart;
}
