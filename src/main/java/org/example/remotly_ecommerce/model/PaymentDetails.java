package org.example.remotly_ecommerce.model;


import lombok.Data;
import org.example.remotly_ecommerce.domain.PaymentStatus;

/**
 * Represents the payment-related information associated with an order.
 * Includes identifiers and statuses returned from the Razorpay payment gateway.
 *
 * This class is typically used as an embedded object in the {@link Order} entity.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Data
public class PaymentDetails {

    /**
     * General payment identifier (could be internal system ID).
     */
    private String paymentId;

    /**
     * Razorpay-specific payment link ID used for tracking.
     */
    private String razorpayPaymentLinkId;

    /**
     * Reference ID used to link the payment to a specific order or transaction.
     */
    private String razorpayPaymentLinkReferenceId;

    /**
     * Current status of the Razorpay payment link (e.g., created, paid, failed).
     */
    private String razorpayPaymentLinkStatus;

    /**
     * Final Razorpay payment transaction ID after successful payment.
     */
    private String razorpayPaymentId;

    /**
     * Status of the payment (e.g., PENDING, PAID, FAILED) as defined in {@link PaymentStatus}.
     */
    private PaymentStatus paymentStatus;
}
