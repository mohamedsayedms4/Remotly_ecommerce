package org.example.remotly_ecommerce.service.user;


import org.example.remotly_ecommerce.dto.user.ChangeUserPWD;
import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.dto.user.UserUpdateDto;
import org.example.remotly_ecommerce.dto.user.UserUpdateProfileImageDto;
import org.example.remotly_ecommerce.exception.InvalidEmail;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {

//    Optional<UserFullInformationDto> findByEmail(String email);
//    Optional<UserFullInformationDto> findById(String id);
    <T> Optional<T> findByEmail(String email, Class<T> returnType);
    <T> Optional<T> findById(String id, Class<T> returnType);
    <T> Optional<T> findByJwt(String jwt, Class<T> returnType);    Optional<UserFullInformationDto> findByPhoneNumber(String phoneNumber);
    Optional<UserUpdateDto> updateUser(UserUpdateDto user , String email) ;
//    Optional<UserFullInformationDto> findById(Long id);
    Optional<UserUpdateProfileImageDto> updateProfileImage(String imageUrl , String email) throws InvalidEmail;
    void deleteUser(Long id);

    Long findUserByEmail(String email);
    Page<UserFullInformationDto> getAllUsers(int page, int size);

    Boolean updatePassword(ChangeUserPWD changeUserPWD);

}
