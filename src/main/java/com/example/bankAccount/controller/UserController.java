package com.example.bankAccount.controller;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.User;
import com.example.bankAccount.repository.UserRepository;
import com.example.bankAccount.service.AccountService;
import com.example.bankAccount.service.UserService;
import com.example.bankAccount.util.AccountNumberGenerator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
//@RequestMapping("/home")
public class UserController {

private final UserService service;

private final UserRepository userRepository;

private final AccountService accountService;


  public UserController(UserService service, UserRepository userRepository,
      AccountService accountService) {
    this.service = service;
    this.userRepository = userRepository;
    this.accountService = accountService;
  }


  private List<User> userList () {
    return service.getAllUsers();
  }

  private String getLoggedInUser () {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println(auth.getDetails());
    return auth.getName(); //get logged in username
  }



  //List<User> users = userList();
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




//  @PostMapping("/account")
//  public String createNewAccount (@RequestParam String accountType,
//      @RequestParam double initialDeposit) {
//    Account account = new Account();
//    account.setAccountNumber( AccountNumberGenerator.generateAccountNumber());
//    account.setBalance(initialDeposit);
//    account.setUser(userRepository.findByUsername(getLoggedInUser()));
//    account.setAccountType(accountType);
//    accountService.saveAccountToDatabase(account);
//    return "redirect:/home";
//  }



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


  //@GetMapping("/")


//  @PostMapping("/login")
//  public String loginUser (@RequestParam String username,
//      @RequestParam String password
//  ) {
//    return "redirect:/home";
//  }

//  @PostMapping("/signUpSuccess")
//  public String loginUserSuccess (@RequestParam String username,
//      @RequestParam String password
//  ) {
//    return "redirect:/home";
//  }

}
