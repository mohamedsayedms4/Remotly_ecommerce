package org.example.remotly_ecommerce.helper.product.view;

import org.example.remotly_ecommerce.model.Product;

public interface ProductViewHelperInterface {
    Product incrementViews(Long productId);
    Long getViews(Long productId);
}
