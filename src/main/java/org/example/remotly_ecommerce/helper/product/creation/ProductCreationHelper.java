package org.example.remotly_ecommerce.helper.product.creation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.product.BaseProductUploadRecord;
import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.exception.CategoryNotFoundException;
import org.example.remotly_ecommerce.exception.ProductCreationException;
import org.example.remotly_ecommerce.model.Category;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.CategoryRepository;
import org.example.remotly_ecommerce.repository.ProductRepository;
import org.example.remotly_ecommerce.utilis.CalculateDiscountPercentage;
import org.example.remotly_ecommerce.utilis.ImageUploadUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service class responsible for creating products.
 * Handles operations such as calculating discount and saving product to the repository.
 *
 * Author: Mohamed Sayed
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCreationHelper implements ProductCreationServiceInterface {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageUploadUtil imageUploadUtil;

    /**
     * Creates a new product for the given seller using the provided product record.
     *
     * @param productRecord DTO containing the product information.
     * @param seller The seller associated with the product.
     * @return The saved Product entity.
     */
    @Override
    @Transactional
    public Product createProduct(BaseProductUploadRecord productRecord, Seller seller) {
        try {
            log.info("Creating product: {} for seller ID: {}", productRecord.title(), seller.getId());

            // Validate input
            validateProductRecord(productRecord);

            int discount = CalculateDiscountPercentage.calculate(
                    productRecord.mrpPrice(),
                    productRecord.sellingPrice()
            );

            return saveProduct(productRecord, seller, discount);

        } catch (Exception e) {
            log.error("Failed to create product: {} for seller ID: {}",
                    productRecord.title(), seller.getId(), e);
            throw new ProductCreationException("Failed to create product: " + productRecord.title(), e);
        }
    }

    private void validateProductRecord(BaseProductUploadRecord productRecord) {
        if (productRecord.title() == null || productRecord.title().trim().isEmpty()) {
            throw new IllegalArgumentException("Product title cannot be null or empty");
        }
        if (productRecord.mrpPrice() == null || productRecord.mrpPrice() <= 0) {
            throw new IllegalArgumentException("MRP price must be positive");
        }
        if (productRecord.sellingPrice() == null || productRecord.sellingPrice() <= 0) {
            throw new IllegalArgumentException("Selling price must be positive");
        }
        if (productRecord.sellingPrice() > productRecord.mrpPrice()) {
            throw new IllegalArgumentException("Selling price cannot be greater than MRP price");
        }
        if (productRecord.categoryId() == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
    }



    /**
     * Saves the product entity to the repository after setting all fields.
     *
     * @param productRecord DTO containing product information.
     * @param seller The seller of the product.
     * @param discountPercentage The calculated discount percentage.
     * @return The saved Product entity.
     */
    private Product saveProduct(BaseProductUploadRecord productRecord, Seller seller, int discountPercentage) {
        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(getCategory(productRecord.categoryId()));
        product.setTitle(productRecord.title());
        product.setDescription(productRecord.description());
        product.setSellingPrice(productRecord.sellingPrice());
        product.setMrpPrice(productRecord.mrpPrice());
        product.setSize(productRecord.size());
        product.setColor(productRecord.color());

        // Handle image upload safely
        if (productRecord.images() != null && productRecord.images().length > 0) {
            product.setImages(imageUploadUtil.saveImages(productRecord.images()));
        }

        product.setDiscountPercentage(discountPercentage);
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully: {} (ID: {})", savedProduct.getTitle(), savedProduct.getId());
        return savedProduct;
    }



    /**
     * Retrieves the Category entity by its ID.
     *
     * @param categoryId The ID of the category.
     * @return The Category entity.
     * @throws RuntimeException if the category is not found.
     */
    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
    }

}
