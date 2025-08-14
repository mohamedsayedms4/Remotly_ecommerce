package org.example.remotly_ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.exception.UserAlreadyExistsException;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.response.LoginRequest;
import org.example.remotly_ecommerce.response.SignUpRequest;
import org.example.remotly_ecommerce.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class Account {


}
