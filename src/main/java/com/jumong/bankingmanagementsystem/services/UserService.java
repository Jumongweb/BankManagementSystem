package com.jumong.bankingmanagementsystem.services;

import com.jumong.bankingmanagementsystem.dtos.request.UserRequest;
import com.jumong.bankingmanagementsystem.dtos.response.BankResponse;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
