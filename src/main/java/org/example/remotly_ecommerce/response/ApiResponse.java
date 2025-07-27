package org.example.remotly_ecommerce.response;


import lombok.Data;

/**
 * A simple response object used to return API messages to the client.
 * Typically used to send success, error, or informational messages.
 *
 * Example:
 * <pre>
 *     ApiResponse response = new ApiResponse();
 *     response.setMessage("Operation completed successfully.");
 * </pre>
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Data
public class ApiResponse {
    /**
     * The message to be returned in the API response.
     */
    private String message;
}
