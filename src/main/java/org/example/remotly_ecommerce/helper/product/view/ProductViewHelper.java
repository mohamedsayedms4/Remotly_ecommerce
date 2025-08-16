package org.example.remotly_ecommerce.helper.product.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

/**
 * Service for managing product views
 * Author: Mohamed Sayed
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductViewHelper implements ProductViewHelperInterface{
    private final ProductRepository productRepository;

    /**
     * Increase the view count of a product by 1
     * @param productId ID of the product
     * @return the updated Product
     */
    public Product incrementViews(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Long newViews = (product.getViews() != null ? product.getViews() : 0) + 1;
        product.setViews(newViews);
        Product updatedProduct = productRepository.save(product);

        log.info("Incremented views for product {}. New views: {}", product.getTitle(), newViews);

        return updatedProduct;
    }

    /**
     * Get the current view count of a product
     * @param productId ID of the product
     * @return current number of views
     */
    public Long getViews(Long productId) {
        return productRepository.findById(productId)
                .map(p -> p.getViews() != null ? p.getViews() : 0)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
    }
}
