package org.example.remotly_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.domain.UserRole;

/**
 * Represents a seller in the system.
 * Contains personal details, business details, bank details, and account status.
 *
 * Each seller has a unique email, a pickup address, and their own account verification status.
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
public class Seller {

    /**
     * Unique identifier for the seller.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the seller.
     */
    private String sellerName;

    /**
     * Phone number of the seller.
     */
    private String phoneNumber;

    /**
     * Email address of the seller (must be unique and not null).
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Encrypted password of the seller.
     */
    private String password;

    /**
     * Embedded business details for the seller (e.g., company name, type, etc.).
     */
    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    /**
     * Embedded bank details for the seller (e.g., account number, IFSC, etc.).
     */
    @Embedded
    private BankDetails bankDetails = new BankDetails();

    /**
     * Address used for product pickup.
     */
    @OneToOne
    private Address pickupAddress = new Address();

    /**
     * Goods and Services Tax Identification Number (GSTIN) of the seller.
     */
    private String GSTIN;

    /**
     * Role of the user (default is ROLE_SELLER).
     */
    private UserRole role = UserRole.ROLE_SELLER;

    /**
     * Whether the seller's email is verified.
     */
    private Boolean isEmailVerified = false;

    /**
     * Current account status of the seller (e.g., PENDING_VERIFICATION).
     */
    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}

