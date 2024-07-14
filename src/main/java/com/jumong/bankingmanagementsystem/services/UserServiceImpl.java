package com.jumong.bankingmanagementsystem.services;

import com.jumong.bankingmanagementsystem.data.models.User;
import com.jumong.bankingmanagementsystem.data.repositories.UserRepository;
import com.jumong.bankingmanagementsystem.dtos.request.CreditRequest;
import com.jumong.bankingmanagementsystem.dtos.request.EnquiryRequest;
import com.jumong.bankingmanagementsystem.dtos.request.UserRequest;
import com.jumong.bankingmanagementsystem.dtos.response.AccountInfo;
import com.jumong.bankingmanagementsystem.dtos.response.BankResponse;
import com.jumong.bankingmanagementsystem.dtos.response.EmailDetails;
import com.jumong.bankingmanagementsystem.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativeNumber(userRequest.getAlternativeNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account has been successfully created\n Your account details: \n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\nAccount number: " + savedUser.getAccountNumber())
                .build();

        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .accountNumber(savedUser.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        Boolean isExistingAccount = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if (!isExistingAccount){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findUserByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(
                        AccountInfo.builder()
                                .accountNumber(foundUser.getAccountNumber())
                                .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
                                .accountBalance(foundUser.getAccountBalance())
                                .build()
                )
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        Boolean isExistingAccount = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isExistingAccount){
            return AccountUtils.ACCOUNT_DOES_NOT_EXIST;
        }
        User foundUser = userRepository.findUserByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditRequest creditRequest) {
        Boolean isExistingAccount = userRepository.existsByAccountNumber(creditRequest.getAccountNumber());
        if (!isExistingAccount){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        double creditAmount = creditRequest.getAmount().doubleValue();

        if (creditAmount < 0){
            return BankResponse.builder()
                    .responseMessage("Invalid Credit Amount")
                    .build();
        }

        User userToCredit = userRepository.findUserByAccountNumber(creditRequest.getAccountNumber());
        BigDecimal balance = userToCredit.getAccountBalance();
        userToCredit.setAccountBalance(balance.add(creditRequest.getAmount()));
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESSFUL_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }


}
