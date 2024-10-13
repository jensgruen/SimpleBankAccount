package com.example.bankAccount.service;


import com.example.bankAccount.entity.Account;
import com.example.bankAccount.repository.AccountRepository;
import com.example.bankAccount.util.DepositTransactionException;
import com.example.bankAccount.util.WithdrawalTransactionException;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {


private final EntityManagerFactory entityManagerFactory;
  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository,
      EntityManagerFactory entityManagerFactory) {
    this.accountRepository = accountRepository;
    this.entityManagerFactory = entityManagerFactory;
  }



  public Account getAccountByAccountNumber (String accountNumber) {
    return accountRepository.findByAccountNumber(accountNumber);
  }

  public Account saveAccountToDatabase (Account account) {
    return accountRepository.save(account);
  }

  public Account depositAccount (String accountNumber, Double depositMoney) throws DepositTransactionException {
    Account account = accountRepository.findByAccountNumber(accountNumber);

    SessionFactory sessionFactory =entityManagerFactory.unwrap(SessionFactory.class);
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    try {  account.setBalance(account.getBalance() + depositMoney);
      accountRepository.save(account);
      tx.commit();
    } catch (Exception e) {
      if (tx != null && account==null) {
        System.out.println("rolling back deposit");
        tx.rollback();
        Throwable throwable = new Throwable();
        throw new DepositTransactionException("deposit failed",throwable);
      }
    } finally {
      session.close();
    }
    return account;

  }



  public Account withdrawAccount (String accountNumber, Double withdrawMoney) throws WithdrawalTransactionException {

    Account account = accountRepository.findByAccountNumber(accountNumber);

    SessionFactory sessionFactory =entityManagerFactory.unwrap(SessionFactory.class);
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    try {  account.setBalance(account.getBalance() - withdrawMoney);
      accountRepository.save(account);
      tx.commit();
    } catch (Exception e) {
      if (tx != null) {
        System.out.println("rolling back");
        tx.rollback();
        Throwable throwable = new Throwable();
        throw new WithdrawalTransactionException("withdrawal failed",throwable);
      }
    } finally {
      session.close();
    }
    return account;
    }

  @Transactional
  /*   Example why transactional is now necessary: if Deposit Account does not exist and the withdrawal account is not <0 then
  the money from the withdrawal account gets subtracted!
  */
  public void transferAccount (String accountNumber, Double transferMoney, String transferAccountNumber)
      throws WithdrawalTransactionException, DepositTransactionException {

          Account sendAccount = withdrawAccount(accountNumber,transferMoney);
          Account receiveAccount = depositAccount(transferAccountNumber, transferMoney);

  }








}
