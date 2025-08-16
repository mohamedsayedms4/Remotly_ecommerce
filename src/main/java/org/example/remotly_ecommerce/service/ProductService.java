package org.example.remotly_ecommerce.service;

import org.example.remotly_ecommerce.dto.product.BaseProductUploadRecord;
import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.dto.product.ProductResponseDto;
import org.example.remotly_ecommerce.exception.ProductException;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {

     Optional<Product> createProduct(BaseProductUploadRecord productDto , Seller seller );

     Optional<Product> updateProduct(Long id ,ProductRecord productDto);

     void deleteProduct(ProductRecord productDto );

     List<ProductRecord> searchProducts();
     Optional<ProductResponseDto> getProductById(Long id) throws ProductException;
     Page<Product> getAllProducts(Long category, String brand, String colors, String sizes,
                                  Integer minPrice, Integer maxPrice, Integer minDiscount,
                                  String stock, String sort, Integer pageNumber);

     List<ProductRecord> getProductsBySellerId(Long sellerId) throws SellerException , ProductException;

      Page<ProductResponseDto> getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize) ;



     }
