package org.example.remotly_ecommerce.service.user.helper.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.exception.UserException;
import org.example.remotly_ecommerce.mapper.UserMapper;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("idUserDtoStrategy")
@RequiredArgsConstructor
@Slf4j
public class IdUserStrategy implements UserRetrievalStrategy {
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * @param input
     * @return
     * @throws UserException
     */
    @Override
    public Optional<UserFullInformationDto> getUser(String input) throws UserException {
        Long id = Long.valueOf(input);
        log.info("Searching for user with id: {}", id);
        Optional<UserFullInformationDto> user = userRepository.findById(id)
                .map(userMapper::toUserFullInformationDtoDto);
        if (user.isEmpty()) {
            log.error("User not found for id: {}", id);
            throw new UserException("User not found for id: " + id);
        }
        log.debug("User found: {}", user);


        return user;
    }
}
