package org.example.remotly_ecommerce.mapper;

import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.model.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    // Entity → DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "sellerName", target = "sellerName")
    @Mapping(source = "isEmailVerified", target = "isEmailVerified")
    @Mapping(source = "sellerPhoneNumber", target = "sellerPhoneNumber")
    @Mapping(source = "businessDetails", target = "businessDetails")
    SellerDto toDto(Seller seller);

    // DTO → Entity
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "sellerName", target = "sellerName")
    @Mapping(source = "isEmailVerified", target = "isEmailVerified")
    @Mapping(source = "sellerPhoneNumber", target = "sellerPhoneNumber")
    @Mapping(source = "businessDetails", target = "businessDetails")
    Seller toEntity(SellerDto dto);

    // List mapping
    List<SellerDto> toDtoList(List<Seller> sellers);
    List<Seller> toEntityList(List<SellerDto> dtos);
}
