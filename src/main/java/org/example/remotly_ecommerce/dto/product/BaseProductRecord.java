package org.example.remotly_ecommerce.dto.product;



import java.util.List;
public record BaseProductRecord(
        Long id,
        String title,
        String description,
        Double mrpPrice,
        Double sellingPrice,
        Double discountPercentage,
        List<String> images,
        String size,
        String color
) {}
