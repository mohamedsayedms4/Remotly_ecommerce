package org.example.remotly_ecommerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.remotly_ecommerce.model.Address;


public record UserUpdateDto(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid customerEmail format")
        String email ,
        @NotBlank(message = "Full name is required")
        @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
        String fullName,
        Address pickupAddress,
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^(\\+20|0)?1[0-9]{9}$", message = "Invalid Egyptian phone number")
        String phoneNumber
) {
}
