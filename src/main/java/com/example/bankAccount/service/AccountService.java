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


  public Account getAccountByAccountNumber (String accountNumber) {
    return accountRepository.findByAccountNumber(accountNumber);
  }

  public Account saveAccountToDatabase (Account account) {
    return accountRepository.save(account);
  }

  public Account depositAccount (String accountNumber, Double depositMoney) {
    Account account = accountRepository.findByAccountNumber(accountNumber);
    account.setBalance(account.getBalance() + depositMoney);
    return accountRepository.save(account);
  }

  public Account withdrawAccount (String accountNumber, Double withdrawMoney) throws Exception {
    Account account = accountRepository.findByAccountNumber(accountNumber);
    if (account.getBalance() - withdrawMoney <0) {
      throw new Exception();
    } else {
      account.setBalance(account.getBalance() - withdrawMoney);
      return accountRepository.save(account);
    }
  }

  @Transactional
  public void transferAccount (String accountNumber, Double transferMoney, String transferAccountNumber)
      throws Exception {

          Account sendAccount = withdrawAccount(accountNumber,transferMoney);
          Account receiveAccount = depositAccount(transferAccountNumber, transferMoney);

  }


}
