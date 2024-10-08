package com.example.bankAccount.util;

public class Round {

  public static double roundTo2Digits (double number) {

    return (double) Math.round(number*100)/100;
  }

}
