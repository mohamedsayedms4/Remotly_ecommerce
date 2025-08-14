package org.example.remotly_ecommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.ProductDto;
import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.exception.ProductException;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.service.ProductService;
import org.example.remotly_ecommerce.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final SellerService sellerService;

    @PostMapping("/insert")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    public ResponseEntity<?> insertProduct(
            @RequestBody ProductDto req,
            @RequestHeader(value = "Authorization", required = false) String jwt) {

        log.info("Received request to insert product: {}", req.getTitle());

        Seller seller = sellerService.getSellerProfile(jwt)
                .orElseThrow(() -> {
                    log.error("Unauthorized or seller not found for JWT: {}", jwt);
                    return new RuntimeException("Seller not found or unauthorized");
                });

        log.info("Seller found: {} (ID: {})", seller.getSellerName(), seller.getId());
        Product product = productService.createProduct(req, seller)
                .orElseThrow(() -> {
                    log.error("Failed to create product for seller ID: {}", seller.getId());
                    return new RuntimeException("Failed to create product");
                });

        log.info("Product created successfully: {} (ID: {})", product.getTitle(), product.getId());

        return ResponseEntity.ok(product);
    }

//    @GetMapping("/seller/{sellerId}")
//    public ResponseEntity<?> getProductsBySellerId(@PathVariable Long sellerId) {
//
//        try {
//            List<ProductDto> products = productService.getProductsBySellerId(sellerId);
//            return ResponseEntity.ok(products);
//        } catch (SellerException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Map.of("error", "Seller not found", "message", e.getMessage()));
//        } catch (ProductException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Map.of("error", "Products not found", "message", e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Internal server error", "message", e.getMessage()));
//        }
//    }



        @GetMapping("/seller/{sellerId}")
        
        public ResponseEntity<List<ProductDto>> getProductsBySellerId(@PathVariable Long sellerId)
                throws SellerException, ProductException {
            List<ProductDto> products = productService.getProductsBySellerId(sellerId);
            return ResponseEntity.ok(products);
        }


}
