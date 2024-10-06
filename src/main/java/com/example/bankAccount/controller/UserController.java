package com.example.bankAccount.controller;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.User;
import com.example.bankAccount.repository.AccountRepository;
import com.example.bankAccount.repository.UserRepository;
import com.example.bankAccount.service.AccountService;
import com.example.bankAccount.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
public class UserController {

private final UserService service;

private final UserRepository userRepository;

private final AccountService accountService;

private final AccountRepository accountRepository;


  public UserController(UserService service, UserRepository userRepository,
      AccountService accountService, AccountRepository accountRepository) {
    this.service = service;
    this.userRepository = userRepository;
    this.accountService = accountService;
    this.accountRepository = accountRepository;
  }


  private List<User> userList () {
    return service.getAllUsers();
  }

  private String getLoggedInUser () {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName(); //get logged in username
  }


  @GetMapping("/home")
  public String home (Model model, @RequestParam(required = false) String accountId) {

    String username = getLoggedInUser();
    User user = userRepository.findByUsername(username);
    List<Account> userAccounts = user.getAccounts();
    List<String> accounts = new ArrayList<>();
    List<Account> accountsFull = new ArrayList<>();

    for (int i=0; i< userAccounts.size(); i++) {
      accounts.add(userAccounts.get(i).getAccountNumber());
      accountsFull.add(userAccounts.get(i));
      if (accountsFull.get(i).getAccountNumber().equals(accountId)) {
        Account account = accountsFull.get(i);
      }
    }
    model.addAttribute("users", accounts);

    return "home";
  }

  @GetMapping("/transactions/deposit")
  public String getDepositMoney (Model model, @RequestParam(required = false) String accountId) {
    String username = getLoggedInUser();
    User user = userRepository.findByUsername(username);
    List<Account> userAccounts = user.getAccounts();
    List<String> accounts = new ArrayList<>();
    List<Account> accountsFull = new ArrayList<>();

    for (int i=0; i< userAccounts.size(); i++) {
      accounts.add(userAccounts.get(i).getAccountNumber());
      accountsFull.add(userAccounts.get(i));
      if (accountsFull.get(i).getAccountNumber().equals(accountId)) {
        Account account = accountsFull.get(i);
      }
    }
    model.addAttribute("users", accounts);

    return "deposit";}

  @PostMapping("/transactions/deposit")
  public String DepositMoney (@RequestParam double depositMoney, @RequestParam String accountNumber) {

    accountService.depositAccount(accountNumber,depositMoney);

    return "redirect:/home";
  }

  @GetMapping("/transactions/withdraw")
  public String getwithdrawMoney (Model model, @RequestParam(required = false) String accountId) {
    String username = getLoggedInUser();
    User user = userRepository.findByUsername(username);
    List<Account> userAccounts = user.getAccounts();
    List<String> accounts = new ArrayList<>();
    List<Account> accountsFull = new ArrayList<>();

    for (int i=0; i< userAccounts.size(); i++) {
      accounts.add(userAccounts.get(i).getAccountNumber());
      accountsFull.add(userAccounts.get(i));
      if (accountsFull.get(i).getAccountNumber().equals(accountId)) {
        Account account = accountsFull.get(i);
      }
    }
    model.addAttribute("users", accounts);

    return "withdraw";}



  @PostMapping("/transactions/withdraw")
  public String withdrawMoney (@RequestParam double withdrawMoney, @RequestParam String accountNumber) {

    accountService.withdrawAccount(accountNumber,withdrawMoney);

    return "redirect:/home";
  }

  @GetMapping("/transactions/transfer")
  public String getTransferMoney (Model model, @RequestParam(required = false) String accountId) {
    String username = getLoggedInUser();
    User user = userRepository.findByUsername(username);
    List<Account> userAccounts = user.getAccounts();
    List<String> accounts = new ArrayList<>();
    List<Account> accountsFull = new ArrayList<>();

    for (int i=0; i< userAccounts.size(); i++) {
      accounts.add(userAccounts.get(i).getAccountNumber());
      accountsFull.add(userAccounts.get(i));
      if (accountsFull.get(i).getAccountNumber().equals(accountId)) {
        Account account = accountsFull.get(i);
      }
    }
    model.addAttribute("users", accounts);

    return "transfer";}


  @PostMapping("/transactions/transfer")
  public String transferMoney (@RequestParam double transferMoney,
      @RequestParam String accountNumber,
      @RequestParam String transferAccountNumber) {

    if (accountRepository.findByAccountNumber(transferAccountNumber) != null) {
      if (accountService.withdrawAccount(accountNumber, transferMoney).getBalance() < 0 &&
          accountRepository.findByAccountNumber(transferAccountNumber) != null) {
        accountService.depositAccount(accountNumber, transferMoney);
        return "redirect:/transactions/transfer?error";
      } else {
        accountService.transferAccount(accountNumber, transferMoney, transferAccountNumber);
        return "redirect:/home";
      }
    } else {
      return "redirect:/transactions/transfer?error1";
    }

  }


  @GetMapping("/signup")
  public String signup () {
    return "signup";
  }

  //@RequestParam(required = false) boolean error
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
