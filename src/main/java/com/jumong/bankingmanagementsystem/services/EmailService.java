package com.jumong.bankingmanagementsystem.services;

import com.jumong.bankingmanagementsystem.dtos.response.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);
}
