package com.example.bankAccount.repository;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
  Account findByAccountNumber(String accountNumber);
  List<Account> findAllByUser(User user);
}
