package org.example.remotly_ecommerce.model;


import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a verification code (OTP) used for verifying either a user or a seller.
 * This entity links the OTP code to an customerEmail and optionally to a user or seller entity.
 *
 * Common use case includes verifying identity during registration or login via customerEmail-based OTP.
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
public class VerificationCode {

    /**
     * The unique identifier for the verification code entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The one-time password (OTP) sent to the user or seller.
     */
    private String otp;

    /**
     * The customerEmail address to which the OTP was sent.
     */
    private String email;

    /**
     * The user associated with the verification code (if applicable).
     */
    @OneToOne
    private User user;

    /**
     * The seller associated with the verification code (if applicable).
     */
    @OneToOne
    private Seller seller;
}
