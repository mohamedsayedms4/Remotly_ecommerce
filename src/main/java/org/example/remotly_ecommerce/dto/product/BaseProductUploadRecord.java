package org.example.remotly_ecommerce.dto.product;

import org.springframework.web.multipart.MultipartFile;

public record BaseProductUploadRecord(
        String title,
        String description,
        Double mrpPrice,
        Double sellingPrice,
        Double discountPercentage,
        MultipartFile[] images,
        String size,
        String color,
        Long categoryId


) {}
