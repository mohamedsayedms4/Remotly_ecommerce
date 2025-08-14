package org.example.remotly_ecommerce.service;

import org.example.remotly_ecommerce.dto.ProductDto;
import org.example.remotly_ecommerce.exception.ProductException;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {

     Optional<Product> createProduct(ProductDto productDto , Seller seller );

     Optional<Product> updateProduct(ProductDto productDto);

     void deleteProduct(ProductDto productDto );

     List<ProductDto> searchProducts();

     public Page<Product> getAllProducts(
             String category ,
             String brand ,
             String colors ,
             String sizes ,
             Integer minPrice ,
             Integer maxPrice ,
             Integer minDiscount ,
             String stock ,
             String sort ,
             Integer pageNumber

     );

     List<ProductDto> getProductsBySellerId(Long sellerId) throws SellerException , ProductException;



}
