package org.example.remotly_ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.ProductDto;
import org.example.remotly_ecommerce.exception.ProductException;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.mapper.ProductMapper;
import org.example.remotly_ecommerce.model.Category;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.CategoryRepository;
import org.example.remotly_ecommerce.repository.ProductRepository;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.example.remotly_ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final ProductMapper productMapper;
    /**
     * @param productDto
     * @param seller
     * @return
     */
    @Override
    public Optional<Product> createProduct(ProductDto productDto, Seller seller) {
        log.info("Creating product: {} for seller ID: {}", productDto.getTitle(), seller.getId());

// البحث عن الفئة الأولى
        Category category1 = categoryRepository.findById(productDto.getCategory().getId()).orElse(null);

        if (category1 == null) {
            log.info("Category1 not found (ID: {}), creating new category.", productDto.getCategory());
            Category category = new Category();
            category.setId(productDto.getCategory().getId());
            category.setLevel(1);
            category.setName("Mobile");
            category1 = categoryRepository.save(category); // category1 الآن من نوع Category
            log.info("Category1 created with ID: {}", category1.getId()); // بدون get()
        } else {
            log.info("Category1 found: ID {}", category1.getId()); // بدون get()
        }


//        Category category2 = categoryRepository.findByCategoryId(productDto.getCategory2());
//        if (category2 == null) {
//            log.info("Category2 not found (ID: {}), creating new category.", productDto.getCategory2());
//            Category category = new Category();
//            category.setCategoryId(productDto.getCategory2());
//            category.setLevel(2);
//            category2 = categoryRepository.save(category);
//            log.info("Category2 created with ID: {}", category2.getCategoryId());
//        } else {
//            log.info("Category2 found: ID {}", category2.getCategoryId());
//        }

//        Category category3 = categoryRepository.findByCategoryId(productDto.getCategory3());
//        if (category3 == null) {
//            log.info("Category3 not found (ID: {}), creating new category.", productDto.getCategory3());
//            Category category = new Category();
//            category.setCategoryId(productDto.getCategory3());
//            category.setLevel(3);
//            category3 = categoryRepository.save(category);
//            log.info("Category3 created with ID: {}", category3.getCategoryId());
//        } else {
//            log.info("Category3 found: ID {}", category3.getCategoryId());
//        }

        int discountPercentage = calculateDiscountPercentage(productDto.getMrpPrice(), productDto.getSellingPrice());
        log.info("Calculated discount percentage: {}%", discountPercentage);

        Product product = new Product();
        product.setCategory(category1);
        product.setSeller(seller);
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(productDto.getTitle());
        product.setColor(productDto.getColor());
        product.setSellingPrice(productDto.getSellingPrice());
        product.setImages(productDto.getImages());
        product.setMrpPrice(productDto.getMrpPrice());
        product.setSize(productDto.getSize());
        product.setDiscountPercentage(discountPercentage);

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully: {} (ID: {})", savedProduct.getTitle(), savedProduct.getId());

        return Optional.of(savedProduct);
    }

    /**
     * @param productDto
     * @return
     */
    @Override
    public Optional<Product> updateProduct(ProductDto productDto) {
        return Optional.empty();
    }

    /**
     * @param productDto
     */
    @Override
    public void deleteProduct(ProductDto productDto) {

    }

    /**
     * @return
     */
    @Override
    public List<ProductDto> searchProducts() {
        return List.of();
    }

    /**
     * @param category
     * @param brand
     * @param colors
     * @param sizes
     * @param minPrice
     * @param maxPrice
     * @param minDiscount
     * @param stock
     * @param sort
     * @param pageNumber
     * @return
     */
    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String stock, String sort, Integer pageNumber) {
        return null;
    }

    /**
     * @param sellerId
     * @return
     */
    @Override
    public List<ProductDto> getProductsBySellerId(Long sellerId) throws SellerException, ProductException {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerException("Seller with ID " + sellerId + " not found"));

        List<Product> products = productRepository.findBySellerId(sellerId);
        if (products.isEmpty()) {
            throw new ProductException("No products found for seller with ID " + sellerId);
        }

        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }



    private int calculateDiscountPercentage(double mrpPrice , double sellingPrice) {
        if(mrpPrice <=0){
            throw new IllegalArgumentException("mrpPrice must be greater than 0");

        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount/mrpPrice)*100;
        return (int)discountPercentage;

    }
}
