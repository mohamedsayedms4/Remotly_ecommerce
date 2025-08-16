package org.example.remotly_ecommerce.utilis;

public class CalculateDiscountPercentage {
    public static int calculate(double mrpPrice, double sellingPrice) {
        if (mrpPrice <= 0) throw new IllegalArgumentException("mrpPrice must be greater than 0");
        double discount = mrpPrice - sellingPrice;
        return (int) ((discount / mrpPrice) * 100);
    }
}
