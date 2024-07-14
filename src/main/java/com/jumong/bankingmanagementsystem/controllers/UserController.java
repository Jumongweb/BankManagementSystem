package com.jumong.bankingmanagementsystem.controllers;

import com.jumong.bankingmanagementsystem.dtos.request.CreditRequest;
import com.jumong.bankingmanagementsystem.dtos.request.EnquiryRequest;
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

    @GetMapping("/balanceEnquiry")
    public BankResponse checkBalance(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/nameEnquiry")
    public String checkNameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditRequest creditRequest ){
        return userService.creditAccount(creditRequest);
    }

}
