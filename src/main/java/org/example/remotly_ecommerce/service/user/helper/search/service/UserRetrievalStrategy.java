package org.example.remotly_ecommerce.service.user.helper.search.service;

import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.exception.UserException;
import org.example.remotly_ecommerce.model.Seller;

import java.util.Optional;

public interface UserRetrievalStrategy<T> {
    Optional<T> getUser(String input) throws UserException;
}