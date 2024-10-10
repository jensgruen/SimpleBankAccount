package com.example.bankAccount.controller;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.service.AccountService;
import com.example.bankAccount.service.UserService;
import com.example.bankAccount.util.Round;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@org.springframework.stereotype.Controller
public class UserController {

private final UserService service;

private final AccountService accountService;



  public UserController(UserService service,
      AccountService accountService) {
    this.service = service;
    this.accountService = accountService;
  }



  @GetMapping("/home")
  public String home (Model model, @RequestParam(required = false) String accountNumber) {

    List<Account> accountsOfLoggedInUser=  service.listAccountsFromLoggedInUser();
    List<String> listAccountNumbers = service.getListOfAccountNumbersFromAccountsFromLoggedInUser(accountsOfLoggedInUser);

    if (accountNumber != null) {
      Account account = accountService.getAccountByAccountNumber(accountNumber);
      model.addAttribute("balance", Round.roundTo2Digits(account.getBalance()));
    }

    model.addAttribute("accountNumbersList", listAccountNumbers);
    model.addAttribute("accountNumber", accountNumber);
    return "home";
  }

  @GetMapping("/transactions/deposit")
  public String getDepositMoney (Model model, @RequestParam(required = false) String accountNumber) {

    List<Account> accountsOfLoggedInUser=  service.listAccountsFromLoggedInUser();
    List<String> listAccountNumbers = service.getListOfAccountNumbersFromAccountsFromLoggedInUser(accountsOfLoggedInUser);
    Account account = accountService.getAccountByAccountNumber(accountNumber);
    model.addAttribute("users", listAccountNumbers);

    return "deposit";}

  @PostMapping("/transactions/deposit")
  public String DepositMoney (@RequestParam double depositMoney, @RequestParam String accountNumber) {

    accountService.depositAccount(accountNumber,depositMoney);

    return "redirect:/home?accountNumber="+accountNumber;
  }

  @GetMapping("/transactions/withdraw")
  public String getWithdrawMoney (Model model, @RequestParam(required = false) String accountNumber) {

    List<Account> accountsOfLoggedInUser=  service.listAccountsFromLoggedInUser();
    List<String> listAccountNumbers = service.getListOfAccountNumbersFromAccountsFromLoggedInUser(accountsOfLoggedInUser);
    Account account = accountService.getAccountByAccountNumber(accountNumber);
    model.addAttribute("users", listAccountNumbers);

    return "withdraw";}


  @PostMapping("/transactions/withdraw")
  public String withdrawMoney (@RequestParam double withdrawMoney,
      @RequestParam String accountNumber) {

    try {
      accountService.withdrawAccount(accountNumber,withdrawMoney);
    } catch (Exception e) {
      return "redirect:/home?error?accountNumber=" +accountNumber;
    }

    return "redirect:/home?accountNumber="+accountNumber;
  }

  @GetMapping("/transactions/transfer")
  public String getTransferMoney (Model model, @RequestParam(required = false) String accountNumber) {

    List<Account> accountsOfLoggedInUser=  service.listAccountsFromLoggedInUser();
    List<String> listAccountNumbers = service.getListOfAccountNumbersFromAccountsFromLoggedInUser(accountsOfLoggedInUser);
    Account account = accountService.getAccountByAccountNumber(accountNumber);
    model.addAttribute("users", listAccountNumbers);

    return "transfer";}


  @PostMapping("/transactions/transfer")
  public String transferMoney (@RequestParam double transferMoney,
      @RequestParam String accountNumber,
      @RequestParam String transferAccountNumber) {

   try {
      accountService.transferAccount(accountNumber, transferMoney, transferAccountNumber);
    } catch (Exception e) {
      return "redirect:/home?transferError?accountNumber=" + accountNumber;
    }

    return "redirect:/home?accountNumber=" + accountNumber;
    }


  @GetMapping("/signup")
  public String signup () {
    return "signup";
  }

  @GetMapping("/login")
  public String login () {
    return "login";
  }


  @PostMapping("/signup")
  public String saveUser (@RequestParam String username,
      @RequestParam String password
  ) {
    if (service.existsByUsername(username)) {
      return "redirect:/signup?error";
    } else {
      service.saveUserInDatabase(username, password);
     return "redirect:/login?success";
    }
  }



}
