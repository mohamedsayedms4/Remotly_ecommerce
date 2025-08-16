package org.example.remotly_ecommerce.helper.product.update;

import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.model.Product;

public interface ProductUpdateServiceInterface {
    Product updateProduct(Long productId, ProductRecord productRecord);

}
