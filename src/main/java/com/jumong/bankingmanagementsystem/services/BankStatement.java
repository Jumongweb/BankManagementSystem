package com.jumong.bankingmanagementsystem.services;

import com.jumong.bankingmanagementsystem.data.models.Transaction;
import com.jumong.bankingmanagementsystem.data.repositories.TransactionRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BankStatement {

    private TransactionRepository transactionRepository;

    public List<Transaction> generateAccountStatement(String accountNumber, String startDate, String endDate){
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository.findAll().stream().filter((transaction)->{transaction.getAccountNumber().equals(accountNumber)})

    }

}
