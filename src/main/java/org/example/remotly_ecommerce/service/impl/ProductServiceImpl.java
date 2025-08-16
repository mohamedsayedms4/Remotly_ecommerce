package org.example.remotly_ecommerce.service.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.product.BaseProductUploadRecord;
import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.dto.product.ProductResponseDto;
import org.example.remotly_ecommerce.exception.ProductException;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.helper.product.creation.ProductCreationServiceInterface;
import org.example.remotly_ecommerce.helper.product.delete.ProductDeleteServiceIInterface;
import org.example.remotly_ecommerce.helper.product.search.ProductSearchHelper;
import org.example.remotly_ecommerce.helper.product.update.ProductUpdateServiceInterface;
import org.example.remotly_ecommerce.mapper.ProductMapper;
import org.example.remotly_ecommerce.model.Category;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.ProductRepository;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.example.remotly_ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCreationServiceInterface productCreationHelper;
    private final ProductUpdateServiceInterface productUpdateHelper;
    private final ProductDeleteServiceIInterface   productDeleteHelper;
    private final ProductSearchHelper productSearchHelper;
    /**
     * @param productDto
     * @param seller
     * @return
     */
    @Override
    public Optional<Product> createProduct(BaseProductUploadRecord productDto, Seller seller) {
        Product product = productCreationHelper.createProduct(productDto, seller);
        return Optional.ofNullable(product);
    }



    /**
     * @param productDto
     * @return
     */
    @Override
    @Transactional
    public Optional<Product> updateProduct(Long id ,ProductRecord productDto) {
        Product product = productUpdateHelper.updateProduct(id ,productDto);
        return Optional.ofNullable(product);
    }

    /**
     * @param productDto
     */
    @Override
    public void deleteProduct(ProductRecord productDto) {

    }

    /**
     * @return
     */
    @Override
    public List<ProductRecord> searchProducts() {
        return List.of();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<ProductResponseDto> getProductById(Long id) throws ProductException {
        Product product = productSearchHelper.getProductById(id);
        if (product == null) {
            return Optional.empty();
        }
        ProductResponseDto dto = productMapper.toResponseDto(product);
        return Optional.of(dto);
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
    public Page<Product> getAllProducts(Long category, String brand, String colors, String sizes,
                                        Integer minPrice, Integer maxPrice, Integer minDiscount,
                                        String stock, String sort, Integer pageNumber) {

        Specification<Product> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                Join<Product, Category> productCategoryJoin = root.join("category");
                predicates.add(cb.equal(productCategoryJoin.get("id"), category));
            }

            if (brand != null && !brand.isBlank()) {
                predicates.add(cb.equal(root.get("brand"), brand));
            }

            if (colors != null && !colors.isBlank()) {
                predicates.add(cb.equal(root.get("color"), colors));
            }

            if (sizes != null && !sizes.isBlank()) {
                predicates.add(cb.equal(root.get("size"), sizes));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }

            if (minDiscount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("discountPercentage"), minDiscount));
            }

            if (stock != null && !stock.isBlank()) {
                if (stock.equalsIgnoreCase("in_stock")) {
                    predicates.add(cb.greaterThan(root.get("quantity"), 0));
                } else if (stock.equalsIgnoreCase("out_of_stock")) {
                    predicates.add(cb.equal(root.get("quantity"), 0));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // الفرز
        String sortBy = "createdAt";
        Sort.Direction direction = Sort.Direction.DESC;

        if (sort != null && !sort.isBlank()) {
            switch (sort.toLowerCase()) {
                case "price_low" -> {
                    sortBy = "sellingPrice";
                    direction = Sort.Direction.ASC;
                }
                case "price_high" -> {
                    sortBy = "sellingPrice";
                    direction = Sort.Direction.DESC;
                }
                case "newest" -> {
                    sortBy = "createdAt";
                    direction = Sort.Direction.DESC;
                }
            }
        }

        Pageable pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by(direction, sortBy));

        return productRepository.findAll(specification, pageable);
    }



    /**
     * @param sellerId
     * @return
     */
    @Override
    public List<ProductRecord> getProductsBySellerId(Long sellerId) throws SellerException, ProductException {
       List<ProductRecord> products = productSearchHelper.getProductsBySellerId(sellerId);
       return products;
    }



    private int calculateDiscountPercentage(double mrpPrice , double sellingPrice) {
        if(mrpPrice <=0){
            throw new IllegalArgumentException("mrpPrice must be greater than 0");

        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount/mrpPrice)*100;
        return (int)discountPercentage;

    }

    @Override
    public Page<ProductResponseDto> getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber != null ? pageNumber : 0,
                pageSize != null ? pageSize : 2,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Specification<Product> spec = (root, query, cb) -> {
            if (categoryId != null) {
                return cb.equal(root.join("category").get("id"), categoryId);
            }
            return cb.conjunction(); // لا فلترة إذا categoryId = null
        };

        Page<Product> productsPage = productRepository.findAll(spec, pageable);

        // تحويل الـ Page<Product> إلى Page<ProductResponseDto>
        return productsPage.map(productMapper::toResponseDto);
    }

}
