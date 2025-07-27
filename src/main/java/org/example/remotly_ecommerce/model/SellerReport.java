package org.example.remotly_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a financial and performance report for a seller.
 * Includes metrics such as earnings, sales, refunds, taxes, and order statistics.
 *
 * This report is associated with a single {@link Seller}.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SellerReport {

    /**
     * Unique identifier for the seller report.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The seller associated with this report.
     */
    @OneToOne
    private Seller seller;

    /**
     * Total earnings of the seller (before deductions).
     */
    private Long totalEarnings = 0L;

    /**
     * Total value of sales made by the seller.
     */
    private Long totalSales = 0L;

    /**
     * Total value of refunds processed.
     */
    private Long totalRefunds = 0L;

    /**
     * Total tax amount deducted from sales.
     */
    private Long totalTax = 0L;

    /**
     * Net earnings after tax and refunds.
     */
    private Long netEarnings = 0L;

    /**
     * Total number of orders placed for this seller.
     */
    private Integer totalOrders = 0;

    /**
     * Total number of canceled orders.
     */
    private Integer canceledOrders = 0;

    /**
     * Total number of financial transactions recorded.
     */
    private Integer totalTransactions = 0;
}
