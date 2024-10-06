package com.example.bankAccount.service;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public Account getAccountById (Integer accountId) {
    return accountRepository.getReferenceById(accountId);
  }

  public Account depositAccount (String accountNumber, Double depositMoney) {
    Account account = accountRepository.findByAccountNumber(accountNumber);
    account.setBalance(account.getBalance() + depositMoney);
    return accountRepository.save(account);
  }


  public Account withdrawAccount (String accountNumber, Double withdrawMoney) {
    Account account = accountRepository.findByAccountNumber(accountNumber);
    account.setBalance(account.getBalance() - withdrawMoney);
    return accountRepository.save(account);
  }

  @Transactional
  public Account transferAccount (String accountNumber, Double transferMoney, String transferAccountNumber) {
    Account account = accountRepository.findByAccountNumber(accountNumber);
    Account transferAccount = accountRepository.findByAccountNumber(transferAccountNumber);
    transferAccount.setBalance(transferAccount.getBalance() + transferMoney);
    return accountRepository.save(transferAccount);
  }





}
