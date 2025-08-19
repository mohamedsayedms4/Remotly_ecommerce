package org.example.remotly_ecommerce.model;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents business-related information for a seller or company,
 * including contact info, address, and media assets like logo and banner.
 *
 * This class can be used to store or transfer business metadata.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Data
public class BusinessDetails {

    /**
     * The name of the business.
     */
    @Column(unique = true)
    private String businessName;

    /**
     * The official customerEmail address of the business.
     */
    @Column(unique = true)
    private String businessEmail;

    /**
     * The mobile phone number for the business.
     */
    @Column(unique = true)

    private String businessMobile;

    /**
     * The physical address of the business.
     */
    private String businessAddress;

    /**
     * The URL or path to the business logo image.
     */
    private String logo;

    /**
     * The URL or path to the business banner image.
     */
    private String banner;
}
