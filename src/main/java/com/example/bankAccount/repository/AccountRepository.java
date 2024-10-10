package com.example.bankAccount.repository;

import com.example.bankAccount.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
  Account findByAccountNumber(String accountNumber);
}
