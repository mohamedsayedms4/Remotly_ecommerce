package org.example.remotly_ecommerce.dto.user;

import jakarta.validation.constraints.NotBlank;
import org.example.remotly_ecommerce.model.Address;


public record UserUpdateProfileImageDto(
        @NotBlank(message = "{image.required}")
        String imageUrl
) {
}
