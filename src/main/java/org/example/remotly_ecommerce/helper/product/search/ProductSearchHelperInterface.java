package org.example.remotly_ecommerce.helper.product.search;

import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.exception.ProductException;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Product;

import java.util.List;

/**
 * Service interface for searching products.
 * Provides methods to search products by ID, seller, category, or keyword.
 *
 * @author Mohamed Sayed
 */
    public interface ProductSearchHelperInterface {
    /**
     * Search for a product by its ID.
     * @param productId the ID of the product
     * @return the product if found, otherwise null or throws exception
     */
    Product getProductById(Long productId) throws ProductException;

    /**
     * Search for products by seller ID.
     * @param sellerId the ID of the seller
     * @return list of products for the given seller
     */
    List<ProductRecord> getProductsBySellerId(Long sellerId)throws SellerException, ProductException;

    /**
     * Search for products by category ID.
     * @param categoryId the ID of the category
     * @return list of products in the given category
     */
    List<Product> getProductsByCategoryId(Long categoryId);

    /**
     * Search for products by keyword in the title or description.
     * @param keyword the search keyword
     * @return list of matching products
     */
    List<Product> searchProductsByKeyword(String keyword);
}
