package org.example.remotly_ecommerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeUserPWD(
        @NotBlank(message = "{email.invalid.blank.input.from.user}")
        @Email(message = "{email.invalid.format.input.from.user}")
        String email,
        @NotBlank(message = "{old.password.required}")
        String password ,
        @NotBlank(message = "{new.password.required}")
        @Size(min = 8, message = "{new.password.size}")
        String newPassword
) {
}
