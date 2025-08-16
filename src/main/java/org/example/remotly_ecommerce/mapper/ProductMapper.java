package org.example.remotly_ecommerce.mapper;

import org.example.remotly_ecommerce.dto.product.BaseProductRecord;
import org.example.remotly_ecommerce.dto.product.ProductRecord;
import org.example.remotly_ecommerce.dto.product.ProductResponseDto;
import org.example.remotly_ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;




@Mapper(componentModel = "spring")
public interface ProductMapper {

    // تحويل من Entity لـ BaseProductRecord
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "mrpPrice", source = "mrpPrice")
    @Mapping(target = "sellingPrice", source = "sellingPrice")
    @Mapping(target = "discountPercentage", source = "discountPercentage")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "color", source = "color")
    BaseProductRecord toBaseRecord(Product product);

    // تحويل من Entity لـ ProductRecord
    default ProductRecord toProductRecord(Product product) {
        return new ProductRecord(
                toBaseRecord(product),       // الحقول المشتركة
                product.getCategory().getId() // categoryId
        );
    }

    // تحويل من Entity لـ ProductResponseDto
    default ProductResponseDto toResponseDto(Product product) {
        return new ProductResponseDto(
                toBaseRecord(product),  // الحقول المشتركة
                product.getCategory(),  // كامل الكائن Category
                product.getViews()      // المشاهدات
        );
    }

}

