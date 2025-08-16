package org.example.remotly_ecommerce.dto.product;

import org.example.remotly_ecommerce.model.Category;

public record ProductResponseDto(
        BaseProductRecord base,
        Category category,
        Long views
) {}