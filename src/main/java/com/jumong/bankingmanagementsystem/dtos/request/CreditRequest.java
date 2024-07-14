package com.jumong.bankingmanagementsystem.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {

    private String accountNumber;
    private BigDecimal amount;
}
