package com.jumong.bankingmanagementsystem.controllers;

import com.jumong.bankingmanagementsystem.dtos.request.UserRequest;
import com.jumong.bankingmanagementsystem.dtos.response.BankResponse;
import com.jumong.bankingmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bank")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public BankResponse createUser(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

}
