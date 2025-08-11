package org.example.remotly_ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Account {

    @PostMapping("/myAccount")
    public String mo(){
        return "حمو بقلظ";
    }
}
