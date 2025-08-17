package org.example.remotly_ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for upgrading a normal user to a seller account.
 * Contains business details provided by the user.
 */
@Valid
public record  BecomeASellerDto(
        @NotBlank(message = "Business name is required")
        String businessName,

        @Email(message = "Invalid business email")
        String businessEmail,

        @NotBlank(message = "Business mobile is required")
        String businessMobile,

        @NotBlank(message = "Business address is required")
        String businessAddress,

        String logo,
        String banner
) {}
