package org.example.remotly_ecommerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.remotly_ecommerce.model.Address;


public record UserUpdateDto(
        @NotBlank(message = "{email.invalid.blank.input.from.user}")
        @Email(message = "{email.invalid.format.input.from.user}")
        String email ,
        @NotBlank(message = "{fullName.required}")
        @Size(min = 3, max = 50, message = "{fullName.size}")
        String fullName,
        Address pickupAddress,
        @NotBlank(message = "{phone.required}")
        @Pattern(regexp = "^(\\+20|0)?1[0-9]{9}$", message = "{phone.invalid}")
        String phoneNumber
) {
}
