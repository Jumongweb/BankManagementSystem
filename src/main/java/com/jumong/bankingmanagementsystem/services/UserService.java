package com.jumong.bankingmanagementsystem.services;

import com.jumong.bankingmanagementsystem.dtos.request.CreditRequest;
import com.jumong.bankingmanagementsystem.dtos.request.EnquiryRequest;
import com.jumong.bankingmanagementsystem.dtos.request.UserRequest;
import com.jumong.bankingmanagementsystem.dtos.response.BankResponse;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditRequest creditRequest);
}
