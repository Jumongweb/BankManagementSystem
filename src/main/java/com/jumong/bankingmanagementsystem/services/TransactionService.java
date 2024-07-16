package com.jumong.bankingmanagementsystem.services;

import com.jumong.bankingmanagementsystem.data.models.Transaction;
import com.jumong.bankingmanagementsystem.dtos.request.TransactionRequest;

public interface TransactionService {
    void saveTransaction(TransactionRequest transactionRequest);
}
