package org.example.remotly_ecommerce.helper.product.delete;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductDeleteService implements ProductDeleteServiceIInterface {
    private final ProductRepository productRepository;

    /**
     * Deletes a product by its ID.
     *
     * @param productId ID of the product to delete.
     * @throws RuntimeException if the product is not found.
     */
    @Override
    public void deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.orElseThrow(() ->
                new RuntimeException("Product not found with ID: " + productId));

        productRepository.delete(product);
        log.info("Product deleted successfully: {} (ID: {})", product.getTitle(), product.getId());
    }
}
