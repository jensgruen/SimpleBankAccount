package com.example.bankAccount.controller;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.repository.UserRepository;
import com.example.bankAccount.service.AccountService;
import com.example.bankAccount.util.AccountNumberGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

  private final AccountService accountService;

  private final UserRepository userRepository;


  public AccountController(AccountService accountService, UserRepository userRepository) {
    this.accountService = accountService;
    this.userRepository = userRepository;
  }

  private String getLoggedInUser () {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println(auth.getDetails());
    return auth.getName(); //get logged in username
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
    account.setUser(userRepository.findByUsername(getLoggedInUser()));
    account.setAccountType(accountType);
    accountService.saveAccountToDatabase(account);
    return "redirect:/home";
  }





}
