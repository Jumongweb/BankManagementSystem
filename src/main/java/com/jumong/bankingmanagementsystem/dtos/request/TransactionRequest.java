package com.jumong.bankingmanagementsystem.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}
