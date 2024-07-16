package com.jumong.bankingmanagementsystem.services;

import com.jumong.bankingmanagementsystem.data.models.User;
import com.jumong.bankingmanagementsystem.data.repositories.UserRepository;
import com.jumong.bankingmanagementsystem.dtos.request.*;
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

    @Autowired
    private TransactionService transactionService;

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
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditRequest.getAmount()));
        userRepository.save(userToCredit);

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(creditRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionRequest);

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

    @Override
    public BankResponse debitAccount(DebitRequest debitRequest) {
        Boolean isExistingAccount = userRepository.existsByAccountNumber(debitRequest.getAccountNumber());
        if (!isExistingAccount){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToDebit = userRepository.findUserByAccountNumber(debitRequest.getAccountNumber());
        if (debitRequest.getAmount().intValue() < 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INVALID_TRANSACTION_CODE)
                    .responseMessage(AccountUtils.INVALID_TRANSACTION_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        if (userToDebit.getAccountBalance().compareTo(debitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                .build();

        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(debitRequest.getAmount()));
            userRepository.save(userToDebit);

            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("CREDIT")
                    .amount(debitRequest.getAmount())
                    .build();

            transactionService.saveTransaction(transactionRequest);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBIT_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }

    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        Boolean senderAccountExist = userRepository.existsByAccountNumber(transferRequest.getSenderAccount());
        Boolean receiverAccountExist = userRepository.existsByAccountNumber(transferRequest.getReceiverAccount());

        if ((!senderAccountExist) || (!receiverAccountExist)){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST)
                    .accountInfo(null)
                    .build();
        }

        User senderAccount = userRepository.findUserByAccountNumber(transferRequest.getSenderAccount());
        User receiverAccount = userRepository.findUserByAccountNumber(transferRequest.getReceiverAccount());

        if (senderAccount.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        senderAccount.setAccountBalance(senderAccount.getAccountBalance().subtract(transferRequest.getAmount()));
        receiverAccount.setAccountBalance(receiverAccount.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(senderAccount);
        userRepository.save(receiverAccount);

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .messageBody("The sum of " + transferRequest.getAmount() + " has been deducted from your account. You current balance is " + senderAccount.getAccountBalance())
                .recipient(senderAccount.getEmail())
                .build();

        emailService.sendEmailAlert(debitAlert);
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(senderAccount.getAccountNumber())
                .transactionType("DEBIT")
                .amount(transferRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionRequest);

        String username = senderAccount.getFirstName() + " " + senderAccount.getLastName() + " " + senderAccount.getOtherName();

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(receiverAccount.getEmail())
                .messageBody("The sum of " + transferRequest.getAmount() + " has been sent to your account from " + username)
                .build();
        emailService.sendEmailAlert(creditAlert);

//        TransactionRequest receivertransactionRequest = TransactionRequest.builder()
//                .accountNumber(receiverAccount.getAccountNumber())
//                .transactionType("CREDIT")
//                .amount(transferRequest.getAmount())
//                .build();
//
//        transactionService.saveTransaction(transactionRequest);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.ACCOUNT_TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(senderAccount.getAccountNumber())
                        .accountName(senderAccount.getFirstName() + " " + senderAccount.getLastName() + " " + senderAccount.getOtherName())
                        .accountBalance(senderAccount.getAccountBalance())
                        .build())
                .build();
    }


}
