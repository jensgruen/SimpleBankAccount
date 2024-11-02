package com.example.bankAccount.service;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.Transfer;
import com.example.bankAccount.entity.TransferDate;
import com.example.bankAccount.repository.TransferDateRepository;
import com.example.bankAccount.repository.TransferRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;


@Service
public class TransferAndTransferDateService {

  private final TransferDateRepository transferDateRepository;

  private final TransferRepository transferRepository;

  public TransferAndTransferDateService(TransferDateRepository transferDateRepository,
      TransferRepository transferRepository) {
    this.transferDateRepository = transferDateRepository;
    this.transferRepository = transferRepository;
  }

  private List<TransferDate> getAllTransferDates () {return transferDateRepository.findAll();}

  private TransferDate addNewTransferDateIfNotExist () {
    List<TransferDate> listOfTransferDates = getAllTransferDates();
    TransferDate transferDateNew =null;
    String transferDate = transferDate();

    if(listOfTransferDates.size() !=0) {
      for (int i = 0; i < listOfTransferDates.size(); i++) {
        if (Objects.equals(listOfTransferDates.get(i).getTransferDate(), transferDate)) {
          transferDateNew = listOfTransferDates.get(i);
          break;
        } else if (i == listOfTransferDates.size() - 1) {
          transferDateNew = new TransferDate(transferDate);
        }
      }
    } else {
      transferDateNew = new TransferDate(transferDate);
    }

    return transferDateNew;
  }

  private void updateTransferListToDate (Transfer transfer, TransferDate transferDateNew) {
    List<Transfer> newTransferList = new ArrayList<>();
    if (transferDateNew.getTransferListToDate() != null) {
      newTransferList = transferDateNew.getTransferListToDate();
    }
    newTransferList.add(transfer);
    transferDateNew.setTransferListToDate(newTransferList);

  }

  public Transfer createAndSaveNewTransfer (String accountNumber, String transferAccountNumber, String transferMoney,
      Account account, Account transferAccount, String accountInfo ) {

    TransferDate transferDateNew = addNewTransferDateIfNotExist();

    Transfer transfer = new Transfer(transferAccountNumber,transferMoney,transferDateNew, account);
    if (accountNumber.equals(transferAccountNumber)) {
      transfer.setAccountInfo(accountInfo);
    } else {
      transfer.setAccountInfo(transferAccount.getUser().getUsername());
    }

    updateTransferListToDate(transfer,transferDateNew);
    transferDateRepository.save(transferDateNew);
    transferRepository.save(transfer);

    return transfer;
  }


  private String transferDate () {
    LocalDate localDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MMMM");
    return localDate.format(formatter);

  }

}
