package org.example.remotly_ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents bank account details for a user or seller,
 * including account number, account holder name, and IFSC code.
 *
 * This class can be embedded or used in payment-related features.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDetails {

    /**
     * The bank account number.
     */
    private String accountNumber;

    /**
     * The full name of the account holder.
     */
    private String accountHolderName;

    /**
     * The IFSC code of the bank branch.
     */
    private String ifscCode;
}

