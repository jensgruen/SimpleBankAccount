package com.example.bankAccount.service;


import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.Transfer;
import com.example.bankAccount.repository.AccountRepository;
import com.example.bankAccount.util.SetUpSession;
import com.example.bankAccount.util.DepositTransactionException;
import com.example.bankAccount.util.WithdrawalTransactionException;
import jakarta.persistence.EntityManagerFactory;
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


  private final AccountRepository accountRepository;


  private final TransferAndTransferDateService transferAndTransferDateService;


  private final EntityManagerFactory entityManagerFactory;


  public AccountService(AccountRepository accountRepository, TransferAndTransferDateService transferAndTransferDateService,
      EntityManagerFactory entityManagerFactory) {
    this.accountRepository = accountRepository;
    this.transferAndTransferDateService = transferAndTransferDateService;
    this.entityManagerFactory = entityManagerFactory;
  }


  public Account getAccountByAccountNumber (String accountNumber) {
    return accountRepository.findByAccountNumber(accountNumber);
  }

  public void saveAccountToDatabase (Account account) {
    accountRepository.save(account);
  }

  public void depositAccount (String accountNumber, double depositMoney, String transferAccountNumber) throws DepositTransactionException {
    Account account = accountRepository.findByAccountNumber(accountNumber);
    Account transferAccount = accountRepository.findByAccountNumber(transferAccountNumber);

    Session session = getSession();
    Transaction tx = session.beginTransaction();

    try { account.setBalance(account.getBalance() + depositMoney);

      Transfer transfer = transferAndTransferDateService.createAndSaveNewTransfer(accountNumber, transferAccountNumber, "-"+ depositMoney,
          account, transferAccount, "Deposit Money");

      updateTransferListForAccount(account, transfer);

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

  }


  public void withdrawAccount (String accountNumber, Double withdrawMoney, String transferAccountNumber) throws WithdrawalTransactionException {

    Account account = accountRepository.findByAccountNumber(accountNumber);
    Account transferAccount = accountRepository.findByAccountNumber(transferAccountNumber);

    Session session = SetUpSession.getSession();
    Transaction tx = session.beginTransaction();

    try { account.setBalance(account.getBalance() - withdrawMoney);

      Transfer transfer = transferAndTransferDateService.
                createAndSaveNewTransfer(accountNumber, transferAccountNumber, "-"+ withdrawMoney,
                account, transferAccount, "Withdraw Money");

      updateTransferListForAccount(account, transfer);

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

  }

   /*  Example why transactional is now necessary: if Deposit Account does not exist and the withdrawal account is not <0 then
       the money from the withdrawal account gets subtracted!  */
  @Transactional
  public void transferAccount (String accountNumber, Double transferMoney, String transferAccountNumber)
      throws WithdrawalTransactionException, DepositTransactionException {
          withdrawAccount(accountNumber,transferMoney, transferAccountNumber);
          depositAccount(transferAccountNumber, transferMoney, accountNumber);
  }


  private void updateTransferListForAccount (Account account, Transfer transfer) {
    List<Transfer> transferList = account.getTransfers();
    transferList.add(transfer);
    account.setTransfers(transferList);
    accountRepository.save(account);
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



 /*

   private String transferDate () {
    LocalDate localDate = LocalDate.now();
    System.out.println("In methods local Date: " + localDate);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MMMM");
    return localDate.format(formatter);

  }



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


  private Transfer createAndSaveNewTransfer (String accountNumber, String transferAccountNumber, String transferMoney,
      Account account, Account transferAccount, String accountInfo ) {

    TransferDate transferDateNew = addNewTransferDateIfNotExist();

    Transfer transfer = new Transfer(transferAccountNumber,transferMoney,transferDateNew, account);
    if (accountNumber.equals(transferAccountNumber)) {
      transfer.setAccountInfo(accountInfo);
    } else {
      transfer.setAccountInfo(transferAccount.getUser().getUsername());
    }

    updateTransferListToDate(transfer,transferDateNew);
    saveTransferDateToDatabase(transferDateNew);
    transferService.saveTransferToDatabase(transfer);

    return transfer;
  }


  private void updateTransferListToDate (Transfer transfer, TransferDate transferDateNew) {
    List<Transfer> newTransferList = new ArrayList<>();
    if (transferDateNew.getTransferListToDate() != null) {
      newTransferList = transferDateNew.getTransferListToDate();
    }
    newTransferList.add(transfer);
    transferDateNew.setTransferListToDate(newTransferList);

  }*/



/*
  public Account withdrawAccount (String accountNumber, Double withdrawMoney, String transferAccountNumber) throws WithdrawalTransactionException {

    Account account = accountRepository.findByAccountNumber(accountNumber);
    Account transferAccount = accountRepository.findByAccountNumber(transferAccountNumber);

    Session session = getSession();
    Transaction tx = session.beginTransaction();

    try {  account.setBalance(account.getBalance() - withdrawMoney);


      String transferDate = transferDate();

      TransferDate transferDateNew = addNewTransferDateIfNotExist(transferDate);

//      List<TransferDate> listOfTransferDates = getAllTransferDates();
//      TransferDate transferDateNew =null;
//
//      if(listOfTransferDates.size() !=0) {
//        for (int i = 0; i < listOfTransferDates.size(); i++) {
//          if (Objects.equals(listOfTransferDates.get(i).getTransferDate(), transferDate)) {
//            transferDateNew = listOfTransferDates.get(i);
//            break;
//          } else if (i == listOfTransferDates.size() - 1) {
//            transferDateNew = new TransferDate(transferDate);
//          }
//        }
//      } else {
//        transferDateNew = new TransferDate(transferDate);
//      }


//      Transfer transfer = new Transfer(transferAccountNumber,"-"+String.valueOf(withdrawMoney),transferDateNew, account);
//      if (accountNumber.equals(transferAccountNumber)) {
//        transfer.setAccountInfo("Withdraw Money");
//      } else {
//        transfer.setAccountInfo(transferAccount.getUser().getUsername());
//      }

      Transfer transfer = createNewTransfer(accountNumber, transferAccountNumber, "-"+String.valueOf(withdrawMoney),
          transferDateNew, account, transferAccount, "Withdraw Money");

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
*/

/*  public Account depositAccount (String accountNumber, double depositMoney, String transferAccountNumber) throws DepositTransactionException {
    Account account = accountRepository.findByAccountNumber(accountNumber);
    Account transferAccount = accountRepository.findByAccountNumber(transferAccountNumber);

    Session session = getSession();
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

  }*/

  private Session getSession () {
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    return  sessionFactory.openSession();
  }



}


