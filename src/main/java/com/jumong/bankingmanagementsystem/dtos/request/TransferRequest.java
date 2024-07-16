package com.jumong.bankingmanagementsystem.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private String senderAccount;
    private String receiverAccount;
    private BigDecimal amount;

}
