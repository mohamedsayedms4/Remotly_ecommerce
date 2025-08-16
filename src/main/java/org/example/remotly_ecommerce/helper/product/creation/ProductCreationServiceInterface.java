package org.example.remotly_ecommerce.helper.product.creation;

import org.example.remotly_ecommerce.dto.product.BaseProductUploadRecord;
import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;

/**
 * Interface for handling product creation operations.
 * Provides a method to create a product given a ProductRecord and a Seller.
 *
 * @author Mohamed Sayed
 */
public interface ProductCreationServiceInterface {

     /**
      * Creates a new Product entity based on the provided ProductRecord and Seller.
      * This method handles setting all necessary fields and calculating any derived values
      * such as discount percentage.
      *
      * @param productRecord The DTO containing product details.
      * @param seller The seller who owns the product.
      * @return The created Product entity.
      */
     Product createProduct(BaseProductUploadRecord productRecord, Seller seller);

}
