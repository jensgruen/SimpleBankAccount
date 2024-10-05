package com.example.bankAccount.service;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  //Save Account to database
  public Account saveAccountToDatabase (Account account) {
    return accountRepository.save(account);
  }



}
