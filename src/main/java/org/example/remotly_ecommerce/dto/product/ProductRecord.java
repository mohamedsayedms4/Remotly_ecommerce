package org.example.remotly_ecommerce.dto.product;

public record ProductRecord(
        BaseProductRecord base,
        Long categoryId
) {}