package com.example.bankAccount.controller;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.service.AccountService;
import com.example.bankAccount.service.UserService;
import com.example.bankAccount.util.AccountNumberGenerator;
import com.example.bankAccount.util.DepositTransactionException;
import com.example.bankAccount.util.WithdrawalTransactionException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

  private final AccountService accountService;



  private final UserService userService;


  public AccountController(AccountService accountService, UserService userService) {
    this.accountService = accountService;
    this.userService = userService;
  }

  @GetMapping("/account")
  public String saveAccount () {
    return "account";
  }

  @PostMapping("/account")
  public String createNewAccount (@RequestParam String accountType,
       @RequestParam double initialDeposit) {
    Account account = new Account();
    account.setAccountNumber( AccountNumberGenerator.generateAccountNumber());
    //account.setBalance(initialDeposit);
    account.setUser(userService.getUser(userService.getLoggedInUser()));
    //account.setUser(userRepository.findByUsername(getLoggedInUser()));
    account.setAccountType(accountType);
    accountService.saveAccountToDatabase(account);
    return "redirect:/home";
  }


  @GetMapping("/transactions/deposit")
  public String getDepositMoney (Model model, @RequestParam(required = false) String accountNumber) {

    List<String> listAccountNumbers = userService.getListOfAccountNumbersFromAccountsFromLoggedInUser();
    Account account = accountService.getAccountByAccountNumber(accountNumber);
    model.addAttribute("users", listAccountNumbers);

    return "deposit";}

  @PostMapping("/transactions/deposit")
  public String DepositMoney (@RequestParam double depositMoney, @RequestParam String accountNumber) {

    accountService.depositAccount(accountNumber, depositMoney, accountNumber);

    return "redirect:/home?accountNumber="+accountNumber;
  }


  @GetMapping("/transactions/withdraw")
  public String getWithdrawMoney (Model model, @RequestParam(required = false) String accountNumber) {

    List<String> listAccountNumbers = userService.getListOfAccountNumbersFromAccountsFromLoggedInUser();
    Account account = accountService.getAccountByAccountNumber(accountNumber);
    model.addAttribute("users", listAccountNumbers);

    return "withdraw";}


  @PostMapping("/transactions/withdraw")
  public String withdrawMoney (@RequestParam double withdrawMoney,
      @RequestParam String accountNumber) {

    try {
      accountService.withdrawAccount(accountNumber,withdrawMoney, accountNumber);
    } catch (Exception e) {
      return "redirect:/home?accountNumber=" + accountNumber +"&withdrawError";
    }

    return "redirect:/home?accountNumber="+accountNumber;
  }

  @GetMapping("/transactions/transfer")
  public String getTransferMoney (Model model, @RequestParam(required = false) String accountNumber) {

    List<String> listAccountNumbers = userService.getListOfAccountNumbersFromAccountsFromLoggedInUser();
    Account account = accountService.getAccountByAccountNumber(accountNumber);
    model.addAttribute("users", listAccountNumbers);

    return "transfer";}


  @PostMapping("/transactions/transfer")
  public String transferMoney (@RequestParam double transferMoney,
      @RequestParam String accountNumber,
      @RequestParam String transferAccountNumber) {

    try {
      accountService.transferAccount(accountNumber, transferMoney, transferAccountNumber);
    } catch (DepositTransactionException e) {
      return "redirect:/home?accountNumber=" + accountNumber + "&depositError";
    } catch (WithdrawalTransactionException e) {
      return "redirect:/home?accountNumber=" + accountNumber +"&withdrawError";
    }

    return "redirect:/home?accountNumber=" + accountNumber;
  }





}
