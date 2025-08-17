package org.example.remotly_ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.product.BaseProductRecord;
import org.example.remotly_ecommerce.dto.product.BaseProductUploadRecord;
import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.dto.product.ProductResponseDto;
import org.example.remotly_ecommerce.exception.ProductException;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.service.ProductService;
import org.example.remotly_ecommerce.service.SellerService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final SellerService sellerService;

//    @PostMapping("/insert")
//    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
//    public ResponseEntity<?> insertProduct(
//            @RequestBody ProductRecord req,
//            @RequestHeader(value = "Authorization", required = false) String jwt) {
//
//        log.info("Received request to insert product: {}", req.base().title());
//
//        Seller seller = sellerService.getSellerProfile(jwt)
//                .orElseThrow(() -> {
//                    log.error("Unauthorized or seller not found for JWT: {}", jwt);
//                    return new RuntimeException("Seller not found or unauthorized");
//                });
//
//        log.info("Seller found: {} (ID: {})", seller.getSellerName(), seller.getId());
//        Product product = productService.createProduct(req, seller)
//                .orElseThrow(() -> {
//                    log.error("Failed to create product for seller ID: {}", seller.getId());
//                    return new RuntimeException("Failed to create product");
//                });
//
//        log.info("Product created successfully: {} (ID: {})", product.getTitle(), product.getId());
//
//        return ResponseEntity.ok(product);
//    }
    /*
@PostMapping(value = "/insert",
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
@PreAuthorize("hasAnyRole('ADMIN','SELLER')")
public ResponseEntity<?> insertProduct(
        @RequestPart(value = "product", required = true) BaseProductUploadRecord req,
        @RequestPart(value = "images", required = true) MultipartFile[] images,
        @RequestHeader(value = "Authorization", required = false) String jwt) {

    log.info("Received request to insert product: {}", req.title());

    Seller seller = sellerService.getSellerProfile(jwt)
            .orElseThrow(() -> new RuntimeException("Seller not found or unauthorized"));

    log.info("Seller found: {} (ID: {})", seller.getSellerName(), seller.getId());

    // دمج الصور مع الـ record
    BaseProductUploadRecord productWithImages = new BaseProductUploadRecord(
            req.title(),
            req.description(),
            req.mrpPrice(),
            req.sellingPrice(),
            req.discountPercentage(),
            images,
            req.size(),
            req.color(),
            req.categoryId()
    );

    Product product = productService.createProduct(productWithImages, seller)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productWithImages));

    log.info("Product created successfully: {} (ID: {})", product.getTitle(), product.getId());

    return ResponseEntity.ok(product);
}


*/
@PostMapping(value = "/insert")
@PreAuthorize("hasAnyRole('ADMIN','SELLER')")
public ResponseEntity<?> insertProduct(
        @RequestParam("product") String productJson,
        @RequestParam("images") MultipartFile[] images,
        @RequestHeader(value = "Authorization", required = false) String jwt)throws SellerException {

    try {
        ObjectMapper mapper = new ObjectMapper();
        BaseProductUploadRecord req = mapper.readValue(productJson, BaseProductUploadRecord.class);

        // باقي الكود كما هو...
        Seller seller = sellerService.getSellerProfile(jwt)
                .orElseThrow(() -> new RuntimeException("Seller not found or unauthorized"));

        BaseProductUploadRecord productWithImages = new BaseProductUploadRecord(
                req.title(),
                req.description(),
                req.mrpPrice(),
                req.sellingPrice(),
                req.discountPercentage(),
                images,
                req.size(),
                req.color(),
                req.categoryId()
        );

        Product product = productService.createProduct(productWithImages, seller)
                .orElseThrow(() -> new RuntimeException("Product creation failed"));

        return ResponseEntity.ok(product);

    } catch (JsonProcessingException e) {
        return ResponseEntity.badRequest().body("Invalid JSON format for product");
    }
}



























    @GetMapping("/seller/{sellerId}")

    public ResponseEntity<List<ProductRecord>> getProductsBySellerId(@PathVariable Long sellerId)
            throws SellerException, ProductException {
        List<ProductRecord> products = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/id")
    public ResponseEntity<ProductResponseDto> getProductById(@RequestParam Long id) throws ProductException {
        log.info("Received request to get product by ID: {}", id);
        ProductResponseDto productDto = productService.getProductById(id)
                .orElseThrow(() -> new ProductException("Product not found with id: " + id));
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String colors,
            @RequestParam(required = false) String sizes,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") Integer pageNumber
    ) {
        log.info("Received request to search products with filters: " +
                        "category={}, brand={}, colors={}, sizes={}, minPrice={}, maxPrice={}, minDiscount={}, stock={}, sort={}, pageNumber={}",
                category, brand, colors, sizes, minPrice, maxPrice, minDiscount, stock, sort, pageNumber
        );

        Page<Product> products = productService.getAllProducts(
                category, brand, colors, sizes,
                minPrice, maxPrice, minDiscount,
                stock, sort, pageNumber
        );

        log.info("Search returned {} products (page {}/{})",
                products.getNumberOfElements(),
                products.getNumber() + 1,
                products.getTotalPages()
        );

        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDto>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "2") Integer size
    ) {
        Page<ProductResponseDto> products = productService.getProductsByCategory(categoryId, page, size);
        return ResponseEntity.ok(products);
    }

}
