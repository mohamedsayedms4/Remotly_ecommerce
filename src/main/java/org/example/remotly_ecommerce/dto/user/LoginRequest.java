package org.example.remotly_ecommerce.dto.user;

/**
 * Represents a login request payload for the authentication process.
 * <p>
 * This record can be used for both standard customerEmail/password login
 * and OTP-based login systems.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Standard login
 *     LoginRequest request = new LoginRequest("user@example.com", "password123");
 *
 *     // OTP login (password field can be used to send OTP if needed)
 *     LoginRequest otpRequest = new LoginRequest("user@example.com", "123456");
 * </pre>
 *
 * <p>This DTO (Data Transfer Object) is sent from client to server
 * when attempting to authenticate a user.</p>
 *
 * <ul>
 *     <li><b>customerEmail:</b> the user's customerEmail address used as the unique identifier.</li>
 *     <li><b>password:</b> the user's password or OTP code for authentication.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
public record LoginRequest(
        String userEmailCredentials,     // user's customerEmail (unique identifier for login)
        String userPasswordCredentials   // user's password or OTP
) {
}
