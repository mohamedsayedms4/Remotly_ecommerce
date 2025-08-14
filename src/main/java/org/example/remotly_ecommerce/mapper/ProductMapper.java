package org.example.remotly_ecommerce.mapper;

import org.example.remotly_ecommerce.dto.ProductDto;
import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.model.Product;
import org.example.remotly_ecommerce.model.Seller;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto dto);

}
