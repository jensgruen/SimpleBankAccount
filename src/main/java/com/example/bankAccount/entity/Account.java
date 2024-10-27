package com.example.bankAccount.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name ="accounts")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name ="account_id")
  private int accountId;

  @Column(name = "account_number", unique = true, nullable = false)
  private String accountNumber;

  @Column(name = "account_type")
  private String accountType;

  private double balance;

  public int getAccountId() {
    return accountId;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) throws Exception {

    this.balance = balance;

    if (balance<0) {
      throw new Exception();
    }

  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @ManyToOne
  private User user;


  @OneToMany
  @JoinColumn(name = "account_id")
  private List<Transfer> transfers;

  public List<Transfer> getTransfers() {
    return transfers;
  }

  public void setTransfers(List<Transfer> transfers) {
    this.transfers = transfers;
  }
}
