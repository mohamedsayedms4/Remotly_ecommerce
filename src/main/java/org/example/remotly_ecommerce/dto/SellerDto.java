package org.example.remotly_ecommerce.dto;

import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.model.BusinessDetails;

public record SellerDto(
        Long id,
        String customerEmail,
        String fullName,
        String phoneNumber,
        String sellerName,
        Boolean isEmailVerified,
        String sellerPhoneNumber,
        BusinessDetails businessDetails
) {}
