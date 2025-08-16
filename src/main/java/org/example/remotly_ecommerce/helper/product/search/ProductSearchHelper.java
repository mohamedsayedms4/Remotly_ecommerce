package org.example.remotly_ecommerce.helper.product.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.ProtocolException;
import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.exception.ProductException;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.helper.product.view.ProductViewHelperInterface;
import org.example.remotly_ecommerce.mapper.ProductMapper;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.ProductRepository;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductSearchHelper implements ProductSearchHelperInterface{
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductViewHelperInterface productViewHelper;
    private final SellerRepository sellerRepository;
    /**
     * @param productId the ID of the product
     * @return
     */
    @Override
    public Product getProductById(Long productId) throws ProductException {
        log.info("Searching product by ID: {}", productId);

        // التأكد من وجود المنتج أولاً
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found with ID: " + productId));

        // زيادة المشاهدات باستخدام الـ Helper
        Product updatedProduct = productViewHelper.incrementViews(productId);

        log.info("Product found: {} | Views: {}", updatedProduct.getTitle(), updatedProduct.getViews());

        return updatedProduct;
    }



    /**
     * @param sellerId the ID of the seller
     * @return
     */
    @Override
    public List<ProductRecord> getProductsBySellerId(Long sellerId) throws SellerException, ProductException{
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerException("Seller with ID " + sellerId + " not found"));

        List<Product> products = productRepository.findBySellerId(sellerId);
        if (products.isEmpty()) {
            throw new ProductException("No products found for seller with ID " + sellerId);
        }

        return products.stream()
                .map(productMapper::toProductRecord)
                .toList();
    }

    /**
     * @param categoryId the ID of the category
     * @return
     */
    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return List.of();
    }

    /**
     * @param keyword the search keyword
     * @return
     */
    @Override
    public List<Product> searchProductsByKeyword(String keyword) {
        return List.of();
    }
}
