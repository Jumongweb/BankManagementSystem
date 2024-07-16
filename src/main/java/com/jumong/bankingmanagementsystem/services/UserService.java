package com.jumong.bankingmanagementsystem.services;

import com.jumong.bankingmanagementsystem.dtos.request.*;
import com.jumong.bankingmanagementsystem.dtos.response.BankResponse;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditRequest creditRequest);

    BankResponse debitAccount(DebitRequest debitRequest);
    BankResponse transfer(TransferRequest transferRequest);


}
