package org.example.remotly_ecommerce.service;


import org.example.remotly_ecommerce.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);
    Optional<User> findByJwt(String jwt);

}
