package com.example.bankAccount.service;


import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.Transfer;
import com.example.bankAccount.entity.TransferDate;
import com.example.bankAccount.repository.AccountRepository;
import com.example.bankAccount.repository.TransferDateRepository;
import com.example.bankAccount.repository.TransferRepository;
import com.example.bankAccount.util.DepositTransactionException;
import com.example.bankAccount.util.WithdrawalTransactionException;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {


private final EntityManagerFactory entityManagerFactory;
  private final AccountRepository accountRepository;

  private final TransferDateRepository transferDateRepository;

  private final TransferService transferService;

  private final TransferRepository transferRepository;

  public AccountService(AccountRepository accountRepository,
      EntityManagerFactory entityManagerFactory, TransferDateRepository transferDateRepository,
      TransferService transferService, TransferRepository transferRepository) {
    this.accountRepository = accountRepository;
    this.entityManagerFactory = entityManagerFactory;
    this.transferDateRepository = transferDateRepository;
    this.transferService = transferService;
    this.transferRepository = transferRepository;
  }



  public Account getAccountByAccountNumber (String accountNumber) {
    return accountRepository.findByAccountNumber(accountNumber);
  }

  public Account saveAccountToDatabase (Account account) {
    return accountRepository.save(account);
  }

  public TransferDate saveTransferDateToDatabase (TransferDate transferDate) {return transferDateRepository.save(transferDate); }


  public TransferDate getTransferDateByDate (String transferDate) {
    return transferDateRepository.findByTransferDate(transferDate);
  }


  public List<TransferDate> getAllTransferDates () {return transferDateRepository.findAll();}

  public Account depositAccount (String accountNumber, double depositMoney, String transferAccountNumber) throws DepositTransactionException {
    Account account = accountRepository.findByAccountNumber(accountNumber);
    Account transferAccount = accountRepository.findByAccountNumber(transferAccountNumber);

    SessionFactory sessionFactory =entityManagerFactory.unwrap(SessionFactory.class);
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    try {  account.setBalance(account.getBalance() + depositMoney);

      String transferDate = transferDate();
      List<TransferDate> listOfTransferDates = getAllTransferDates();
      TransferDate transferDateNew =null;

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


      Transfer transfer = new Transfer(transferAccountNumber,"+"+String.valueOf(depositMoney),transferDateNew, account);
      if (accountNumber.equals(transferAccountNumber)) {
        transfer.setAccountInfo("Deposit Money");
      } else {
        transfer.setAccountInfo(transferAccount.getUser().getUsername());
      }
      System.out.println("transferlist before: "+ transferDateNew.getTransferListToDate());
      List<Transfer> newTransferList = new ArrayList<>();
      if (transferDateNew.getTransferListToDate() == null) {
        newTransferList.add(transfer);
        transferDateNew.setTransferListToDate(newTransferList);
      } else {
        newTransferList = transferDateNew.getTransferListToDate();
        newTransferList.add(transfer);
        transferDateNew.setTransferListToDate(newTransferList);
      }

      saveTransferDateToDatabase(transferDateNew);
      transferService.saveTransferToDatabase(transfer);

      List<Transfer> transferList = account.getTransfers();
      transferList.add(transfer);
      account.setTransfers(transferList);
      accountRepository.save(account);

      List<Transfer> dateTransferList = transferDateNew.getTransferListToDate();


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



  public Account withdrawAccount (String accountNumber, Double withdrawMoney, String transferAccountNumber) throws WithdrawalTransactionException {

    Account account = accountRepository.findByAccountNumber(accountNumber);
    Account transferAccount = accountRepository.findByAccountNumber(transferAccountNumber);

    SessionFactory sessionFactory =entityManagerFactory.unwrap(SessionFactory.class);
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    try {  account.setBalance(account.getBalance() - withdrawMoney);


      String transferDate = transferDate();

      List<TransferDate> listOfTransferDates = getAllTransferDates();
      TransferDate transferDateNew =null;

      if(listOfTransferDates.size() !=0) {
        for (int i = 0; i < listOfTransferDates.size(); i++) {
          System.out.println("loop " + i);
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


      Transfer transfer = new Transfer(transferAccountNumber,"-"+String.valueOf(withdrawMoney),transferDateNew, account);
      if (accountNumber.equals(transferAccountNumber)) {
        transfer.setAccountInfo("Withdraw Money");
      } else {
        transfer.setAccountInfo(transferAccount.getUser().getUsername());
      }

      List<Transfer> newTransferList = new ArrayList<>();
      if (transferDateNew.getTransferListToDate() == null) {
        newTransferList.add(transfer);
        transferDateNew.setTransferListToDate(newTransferList);
      } else {
        newTransferList = transferDateNew.getTransferListToDate();
        newTransferList.add(transfer);
        transferDateNew.setTransferListToDate(newTransferList);
      }

      saveTransferDateToDatabase(transferDateNew);


      transferService.saveTransferToDatabase(transfer);

      List<Transfer> transferList = account.getTransfers();
      transferList.add(transfer);
      account.setTransfers(transferList);
      accountRepository.save(account);

      List<Transfer> dateTransferList = transferDateNew.getTransferListToDate();


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

   /*  Example why transactional is now necessary: if Deposit Account does not exist and the withdrawal account is not <0 then
       the money from the withdrawal account gets subtracted!  */
  @Transactional
  public void transferAccount (String accountNumber, Double transferMoney, String transferAccountNumber)
      throws WithdrawalTransactionException, DepositTransactionException {
          Account sendAccount = withdrawAccount(accountNumber,transferMoney, transferAccountNumber);
          Account receiveAccount = depositAccount(transferAccountNumber, transferMoney, accountNumber);
  }


  private String transferDate () {
    LocalDate localDate = LocalDate.now();
    System.out.println("In methods local Date: " + localDate);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MMMM");
    return localDate.format(formatter);

  }

  public Map<String, List<Transfer>> createMapOfDatesAndListsOfTransfers(String accountNumber) {

    //get current Account, assuming Account does exist -> error handling later
    Account account = accountRepository.findByAccountNumber(accountNumber);
    //get all Transfers from this account
    List<Transfer> transfersForThisAccount = account.getTransfers();

    if (transfersForThisAccount != null) {
      //get all Dates in a set (no duplicates!)
      Set<String> dates = new HashSet<>();
      for (Transfer transfer : transfersForThisAccount) {
        String transferDate = transfer.getTransferDate().getTransferDate();
        dates.add(transferDate);
      }

      //Create map with a String date as key and a list<Transfer> as a value
      Iterator<String> it = dates.iterator();
      Map<String, List<Transfer>> mapWithTransferForEachDate = new HashMap<>();
      while (it.hasNext()) {
        String transferDate = it.next();
        //Create a list of transfers for current date and current account
        List<Transfer> transfersForEachDate = new ArrayList<>();
        for (Transfer transfer : transfersForThisAccount) {
          if (Objects.equals(transfer.getTransferDate().getTransferDate(),
              transferDate)) {
            transfersForEachDate.add(transfer);
          }
        }
        mapWithTransferForEachDate.put(transferDate, transfersForEachDate);
      }
      return mapWithTransferForEachDate;
    }
    return null;
  }








}
