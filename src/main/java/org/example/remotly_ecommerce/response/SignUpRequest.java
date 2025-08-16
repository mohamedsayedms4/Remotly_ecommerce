package org.example.remotly_ecommerce.response;


import lombok.Data;

/**
 * Represents a sign-up request payload containing the user's customerEmail, full name, and one-time password (OTP).
 * This class is typically used in user or seller registration with OTP verification.
 *
 * Example usage:
 * <pre>
 *     SignUpRequest request = new SignUpRequest();
 *     request.setEmail("newuser@example.com");
 *     request.setFullName("Mohamed Sayed");
 *     request.setOtp("654321");
 * </pre>
 *
 * This DTO (Data Transfer Object) is used by sign-up endpoints to validate user input.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Data
public class SignUpRequest {

    /**
     * The customerEmail address provided by the new user.
     */
    private String email;

    /**
     * The full name of the user.
     */
    private String fullName;

    /**
     * The one-time password sent to the user's customerEmail for sign-up verification.
     */
    private String password;


    private String phoneNumber;
}
