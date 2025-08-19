package org.example.remotly_ecommerce.service.user.helper.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.exception.UserException;
import org.example.remotly_ecommerce.mapper.UserMapper;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.example.remotly_ecommerce.utilis.JwtUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("jwtUserDtoStrategy")
@RequiredArgsConstructor
@Slf4j

public class JwtUserDtoStrategy   implements UserRetrievalStrategy {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    /**
     * @return
     * @throws SellerException
     */


    /**
     * @param input
     * @return UserFullInformationDto
     * @throws UserException
     */
    @Override
    public Optional<UserFullInformationDto> getUser(String input) throws UserException {
        String email = jwtUtil.extractEmailFromJwt(input);
        return  userRepository.findByEmail(email)
                .map(userMapper::toUserFullInformationDtoDto);

    }
}
