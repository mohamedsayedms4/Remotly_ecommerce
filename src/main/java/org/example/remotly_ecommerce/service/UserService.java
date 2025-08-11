package org.example.remotly_ecommerce.service;


import jakarta.validation.Valid;
import org.example.remotly_ecommerce.model.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);
    Optional<User> findByJwt(String jwt);

}
