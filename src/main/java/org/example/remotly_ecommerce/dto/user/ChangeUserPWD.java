package org.example.remotly_ecommerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeUserPWD(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid customerEmail format")
        String email,
        @NotBlank(message = "old Password is required")
        String password ,
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters")
        String newPassword
) {
}
