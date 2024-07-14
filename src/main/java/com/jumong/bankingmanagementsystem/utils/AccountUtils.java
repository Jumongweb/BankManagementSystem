package com.jumong.bankingmanagementsystem.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";

    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created!";

    public static final String ACCOUNT_CREATION_SUCCESS = "002";

    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";

    public static final String ACCOUNT_DOES_NOT_EXIST = "003";

    public static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "Account does not exist!";

    public static final String ACCOUNT_FOUND_CODE = "004";

    public static final String ACCOUNT_FOUND_MESSAGE = "User Account Found";
    public static final String ACCOUNT_CREDIT_SUCCESSFUL_CODE = "005";
    public static final String ACCOUNT_CREDIT_SUCCESSFUL_MESSAGE = "CREDIT SUCCESSFUL";

    public static String generateAccountNumber() {
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNumber).toString();
    }

}
