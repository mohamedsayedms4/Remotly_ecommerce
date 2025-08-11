package org.example.remotly_ecommerce.response;


import lombok.Data;

/**
 * Represents a login request payload containing the user's email and one-time password (OTP).
 * This class is typically used in OTP-based login systems.
 *
 * Example usage:
 * <pre>
 *     LoginRequest request = new LoginRequest();
 *     request.setEmail("user@example.com");
 *     request.setOtp("123456");
 * </pre>
 *
 * This DTO (Data Transfer Object) is consumed by authentication endpoints.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Data
public class LoginRequest {

    /**
     * The email address of the user attempting to log in.
     */
    public String email;

    /**
     * The one-time password sent to the user's email for verification.
     */
    private String password;
}
