package org.example.remotly_ecommerce.helper.product.update;

import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.model.Product;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.model.Category;
import org.example.remotly_ecommerce.repository.CategoryRepository;
import org.example.remotly_ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service class responsible for updating existing products.
 * Handles operations like updating product details and recalculating discount.
 *
 * Author: Mohamed Sayed
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductUpdateService implements ProductUpdateServiceInterface {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Updates an existing product using the provided ProductRecord.
     *
     * @param productId The ID of the product to update.
     * @param productRecord DTO containing updated product information.
     * @return The updated Product entity.
     * @throws RuntimeException if the product or category is not found.
     */
    @Override
    public Product updateProduct(Long productId, ProductRecord productRecord) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.orElseThrow(() ->
                new RuntimeException("Product not found with ID: " + productId));

        // Update fields
        product.setTitle(productRecord.base().title());
        product.setDescription(productRecord.base().description());
        product.setSellingPrice(productRecord.base().sellingPrice());
        product.setMrpPrice(productRecord.base().mrpPrice());
        product.setSize(productRecord.base().size());
        product.setColor(productRecord.base().color());
        product.setImages(productRecord.base().images());
        product.setCategory(getCategory(productRecord.categoryId()));

        // Recalculate discount
        int discount = calculateDiscountPercentage(
                productRecord.base().mrpPrice(),
                productRecord.base().sellingPrice()
        );
        product.setDiscountPercentage(discount);
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully: {} (ID: {})", updatedProduct.getTitle(), updatedProduct.getId());
        return updatedProduct;
    }

    /**
     * Calculates discount percentage based on MRP and selling price.
     *
     * @param mrpPrice Maximum retail price.
     * @param sellingPrice Selling price.
     * @return Discount percentage.
     */
    private int calculateDiscountPercentage(double mrpPrice, double sellingPrice) {
        if (mrpPrice <= 0) throw new IllegalArgumentException("mrpPrice must be greater than 0");
        double discount = mrpPrice - sellingPrice;
        return (int) ((discount / mrpPrice) * 100);
    }

    /**
     * Retrieves the Category entity by ID.
     *
     * @param categoryId ID of the category.
     * @return The Category entity.
     * @throws RuntimeException if the category is not found.
     */
    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
    }
}

