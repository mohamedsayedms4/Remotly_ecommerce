package org.example.remotly_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.domain.UserRole;

/**
 * Represents a seller in the system.
 * Contains personal details, business details, bank details, and account status.
 *
 * Each seller has a unique customerEmail, a pickup address, and their own account verification status.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Entity
@Table(name = "sellers")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller extends User {

    private String sellerName;

    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    private String GSTIN;

    private Boolean isEmailVerified = false;


    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus ;

    // في حالة أن الهاتف خاص بالـ Seller فقط
    private String sellerPhoneNumber;
    // ممكن تحذفه من User وتضعه هنا، أو تعيد استخدام الهاتف من User
}
