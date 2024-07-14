package com.jumong.bankingmanagementsystem.data.repositories;

import com.jumong.bankingmanagementsystem.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);
    User findUserByAccountNumber(String accountNumber);
}
