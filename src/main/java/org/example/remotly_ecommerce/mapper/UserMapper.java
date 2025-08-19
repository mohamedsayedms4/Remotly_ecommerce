package org.example.remotly_ecommerce.mapper;

import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps SignUpRequest to User entity.
     * Excludes sensitive fields and collections that should be initialized separately.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "customerPassword") // Password should be encoded separately
    @Mapping(target = "usedCoupons", ignore = true)
    @Mapping(target = "pickupAddress", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "email", source = "customerEmail")
    @Mapping(target = "fullName", source = "customerFullName")
    @Mapping(target = "phoneNumber", source = "customerPhoneNumber")
    @Mapping(target = "imageUrl", source = "customerProfileImage")
    User toUser(SignUpRequest request);

    /**
     * Converts MultipartFile to its Cloudinary URL.
     */
    default String map(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        // هنا تستخدم ImageUploadUtil لرفع الصورة
        // لكن Mapper مش مفروض يعرف الـ Service، ممكن تعطيه URL مباشرة من Service
        return file.getOriginalFilename(); // مؤقتاً فقط، ستستبدل بالرفع في Service
    }
    /**
     * Maps LoginRequest to User entity (minimal mapping for authentication purposes).
     * Only maps the email field as that's typically what's needed for user lookup.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "usedCoupons", ignore = true)
    @Mapping(target = "pickupAddress", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "email", source = "userEmailCredentials")
    @Mapping(target = "password", source = "userPasswordCredentials")
    User toUser(LoginRequest request);

    UserFullInformationDto toUserFullInformationDtoDto(User user);


}
