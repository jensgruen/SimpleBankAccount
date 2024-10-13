package com.example.bankAccount.util;

import jakarta.transaction.TransactionalException;

public class DepositTransactionException extends TransactionalException {

  public DepositTransactionException(String s, Throwable throwable) {
    super(s, throwable);
  }
}

