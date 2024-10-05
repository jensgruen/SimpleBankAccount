package com.example.bankAccount.util;

import java.util.Random;

public class AccountNumberGenerator {

  public static final int ACCOUNT_NUMBER_LENGTH = 12;

  public static String generateAccountNumber() {
    Random random = new Random();
    StringBuilder accountNumber = new StringBuilder();

    for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
      int digit = random.nextInt(10);
      accountNumber.append(digit);
    }

    return accountNumber.toString();
  }

}
