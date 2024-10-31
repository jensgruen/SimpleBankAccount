package com.example.bankAccount.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class TransferDate {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name ="transferDate_Id")
  private int transferDateId;

  private String transferDate;

  public TransferDate() {
  }

  public void setTransferDate(String transferDate) {
    this.transferDate = transferDate;
  }

  public TransferDate(String transferDate) {
    this.transferDate = transferDate;
  }

  public void setTransferListToDate(
      List<Transfer> transferListToDate) {
    this.transferListToDate = transferListToDate;
  }

  public List<Transfer> getTransferListToDate() {
    return transferListToDate;
  }

  public String getTransferDate() {
    return transferDate;
  }

  @OneToMany
  @JoinColumn(name = "transferDate_Id")
  private List<Transfer> transferListToDate;




}
