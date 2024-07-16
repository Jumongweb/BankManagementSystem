package com.jumong.bankingmanagementsystem.data.repositories;

import com.jumong.bankingmanagementsystem.data.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
