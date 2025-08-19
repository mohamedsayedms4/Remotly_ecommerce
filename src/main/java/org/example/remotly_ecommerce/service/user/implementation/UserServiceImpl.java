package org.example.remotly_ecommerce.service.user.implementation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.constants.ApplicationConstants;
import org.example.remotly_ecommerce.dto.user.ChangeUserPWD;
import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.dto.user.UserUpdateDto;
import org.example.remotly_ecommerce.dto.user.UserUpdateProfileImageDto;
import org.example.remotly_ecommerce.exception.InvalidEmail;
import org.example.remotly_ecommerce.exception.InvalidPhoneNumber;
import org.example.remotly_ecommerce.exception.UserException;
import org.example.remotly_ecommerce.mapper.UserMapper;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.example.remotly_ecommerce.service.user.UserService;
import org.example.remotly_ecommerce.service.user.helper.search.service.UserSearchContext;
import org.example.remotly_ecommerce.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserSearchContext  userSearchContext;
    private final PasswordEncoder passwordEncoder;

    @Value("${" + ApplicationConstants.JWT_SECRET_KEY + ":" + ApplicationConstants.JWT_SECRET_DEFAULT_VALUE + "}")
    private String jwtSecret;

    // الـ Generic methods الجديدة
    @Override
    public <T> Optional<T> findByEmail(String email, Class<T> returnType) {
        return userSearchContext.execute("emailUserDtoStrategy", email, returnType);
    }

    @Override
    public <T> Optional<T> findById(String id, Class<T> returnType) {
        return userSearchContext.execute("idUserDtoStrategy", id, returnType);
    }

    @Override
    public <T> Optional<T> findByJwt(String jwt, Class<T> returnType) {
        String strategyName = getStrategyName("jwt", returnType);
        return userSearchContext.execute(strategyName, jwt, returnType);
    }

    // Helper method لتحديد الـ strategy name
    private <T> String getStrategyName(String searchType, Class<T> returnType) {
        String suffix = returnType == User.class ? "EntityStrategy" : "DtoStrategy";
        return searchType + "User" + suffix;
    }



    /**
     * @param phoneNumber
     * @return
     */
    @Override
    public Optional<UserFullInformationDto> findByPhoneNumber(String phoneNumber) {
        return Optional.empty();
    }

    /**
     * @param
     * @return
     */
    @Override
    @Transactional
    public Optional<UserUpdateDto> updateUser(UserUpdateDto dto, String email) throws InvalidEmail , InvalidPhoneNumber {
        log.info("Attempting to update user with email [{}]", email);

        if (dto == null) {
            log.warn("UserUpdateDto is null, update aborted");
            return Optional.empty();
        }

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new InvalidEmail("User with email [" + dto.email() + "] already exists");
        }

        if (userRepository.existsByPhoneNumber(dto.phoneNumber())) {
            throw new InvalidPhoneNumber("User with phone number [" + dto.phoneNumber() + "] already exists");
        }



        return userRepository.findByEmail(email)
                .map(user -> {
                    log.debug("User found in database: {}", user);

                    // تحديث الحقول
                    log.debug("Updating fields for user [{}]", email);
                    user.setEmail(dto.email());
                    user.setFullName(dto.fullName());
                    user.setPickupAddress(dto.pickupAddress());
                    user.setPhoneNumber(dto.phoneNumber());

                    // حفظ التحديث
                    User savedUser = userRepository.save(user);
                    log.info("User [{}] updated successfully", email);

                    // رجّع نسخة DTO من الـ user المحدث
                    UserUpdateDto updatedDto = new UserUpdateDto(
                            savedUser.getEmail(),
                            savedUser.getFullName(),
                            savedUser.getPickupAddress(),
                            savedUser.getPhoneNumber()
                    );

                    log.debug("Returning updated UserUpdateDto: {}", updatedDto);
                    return updatedDto;
                });
    }

    /**
     * @param id
     * @return
     */

    /**
     * @param
     * @return
     */
    @Override
    @Transactional
    public Optional<UserUpdateProfileImageDto> updateProfileImage(String imageUrl, String email) {
        log.info("Attempting to update user profile image [{}]", email);

        return userRepository.findByEmail(email)
                .map(user -> {
                    log.debug("User found in database: {}", user);

                    // تحديث الصورة
                    log.debug("Updating profile image for user [{}]", email);
                    user.setImageUrl(imageUrl);

                    // حفظ التحديث
                    User savedUser = userRepository.save(user);
                    log.info("User [{}] profile image updated successfully", email);

                    // رجّع DTO فيه البيانات الجديدة
                    return new UserUpdateProfileImageDto(
                            savedUser.getImageUrl()
                    );
                });
    }

    /**
     * @param id
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Attempting to delete user with id: {}", id);

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.error("User not found with id: {}", id);
            throw new UserException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("User with id: {} deleted successfully", id);
    }

    /**
     * @param email
     * @return
     */
    @Override
    public Long findUserByEmail(String email) {
       return userRepository.findByEmail(email).map(User::getId).orElse(null);
    }

    /**
     * @param email
     * @return
     */



    /**
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<UserFullInformationDto> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).map(userMapper::toUserFullInformationDtoDto);
    }

    /**
     * @param userDto
     * @return
     */
    @Override
    @Transactional
    public Boolean updatePassword(ChangeUserPWD userDto) {
        User user = userRepository.findByEmail(userDto.email()).
                orElseThrow(()-> new UserException("User with email [" + userDto.email() + "] not found"));

        if(!passwordEncoder.matches(userDto.password(), user.getPassword())) {
            throw new UserException("Old password is incorrect");
        }
        String newPassword = userDto.newPassword();

        String encodedPassword = passwordEncoder.encode(newPassword);
        // 5- تحديث الباسورد
        user.setPassword(encodedPassword);
        userRepository.save(user);

        log.info("Password updated successfully for user: {}", user.getEmail());
        return true;

    }

}