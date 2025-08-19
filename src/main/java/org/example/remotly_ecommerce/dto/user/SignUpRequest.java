package org.example.remotly_ecommerce.dto.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.remotly_ecommerce.domain.UserRole;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * DTO for capturing user sign-up information.
 * This record is used to transfer data from client requests to backend services.
 */
public record SignUpRequest(
        /**
         * User's email address.
         * Must be provided and follow a valid email format.
         */
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid customerEmail format")
        String customerEmail,

        /**
         * User's full name.
         * Must be provided and between 3 and 50 characters.
         */
        @NotBlank(message = "Full name is required")
        @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
        String customerFullName,

        /**
         * User's phone number.
         * Must be provided and match Egyptian phone number format.
         */
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^(\\+20|0)?1[0-9]{9}$", message = "Invalid Egyptian phone number")
        String customerPhoneNumber,

        /**
         * URL or path to the user's profile image.
         * Optional field.
         */
//        MultipartFile customerProfileImage,
        String customerProfileImage,

        /**
         * Raw password provided by the user.
         * Must not be blank and will be encoded before storing in the database.
         */
        @NotBlank
        String customerPassword


) {
}

