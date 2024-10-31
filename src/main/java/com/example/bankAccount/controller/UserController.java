package com.example.bankAccount.controller;

import com.example.bankAccount.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@org.springframework.stereotype.Controller
public class UserController {

private final UserService userService;




  public UserController(UserService userService) {
    this.userService = userService;
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
    if (userService.doesUserExits(username)) {
      return "redirect:/signup?error";
    } else {
      userService.saveUserInDatabase(username, password);
     return "redirect:/login?success";
    }
  }


/*  @GetMapping("/transactions/deposit")
  public String getDepositMoney (Model model, @RequestParam(required = false) String accountNumber) {

    List<Account> accountsOfLoggedInUser=  service.listAccountsFromLoggedInUser();
    List<String> listAccountNumbers = service.getListOfAccountNumbersFromAccountsFromLoggedInUser(accountsOfLoggedInUser);
    Account account = accountService.getAccountByAccountNumber(accountNumber);
    model.addAttribute("users", listAccountNumbers);

    return "deposit";}

  @PostMapping("/transactions/deposit")
  public String DepositMoney (@RequestParam double depositMoney, @RequestParam String accountNumber) {

    accountService.depositAccount(accountNumber, depositMoney, accountNumber);

    return "redirect:/home?accountNumber="+accountNumber;
  }*/


/*  @GetMapping("/transactions/withdraw")
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
      accountService.withdrawAccount(accountNumber,withdrawMoney, accountNumber);
    } catch (Exception e) {
      return "redirect:/home?accountNumber=" + accountNumber +"&withdrawError";
    }

    return "redirect:/home?accountNumber="+accountNumber;
  }*/

/*  @GetMapping("/transactions/transfer")
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
    } catch (DepositTransactionException e) {
      return "redirect:/home?accountNumber=" + accountNumber + "&depositError";
    } catch (WithdrawalTransactionException e) {
      return "redirect:/home?accountNumber=" + accountNumber +"&withdrawError";
    }

    return "redirect:/home?accountNumber=" + accountNumber;
  }*/


/*  @GetMapping("/home")
  public String home (Model model, @RequestParam(required = false) String accountNumber) {

    List<Account> accountsOfLoggedInUser = service.listAccountsFromLoggedInUser();
    List<String> listAccountNumbers = service.getListOfAccountNumbersFromAccountsFromLoggedInUser(
        accountsOfLoggedInUser);

    List<Transfer> listOfTransfers = null;
    List<TransferDate> transferDateList = null;
    Map<String,List<Transfer>> transfersForEachDate = null;
    if (accountNumber != null) {
      Account account = accountService.getAccountByAccountNumber(accountNumber);
      listOfTransfers = accountService.getAccountByAccountNumber(
              accountNumber)
          .getTransfers();
      model.addAttribute("balance", Round.roundTo2Digits(account.getBalance()));

      //need map with date as key and List<Transfer> as values
      transfersForEachDate = accountService.createMapOfDatesAndListsOfTransfers(accountNumber);
      for (Entry<String, List<Transfer>> entry : transfersForEachDate.entrySet()) {
        System.out.println(entry);
      }

    }

    model.addAttribute("accountNumbersList", listAccountNumbers);
    model.addAttribute("accountNumber", accountNumber);
    model.addAttribute("listOfTransfers", listOfTransfers);
    model.addAttribute("dateTransferMap", transfersForEachDate);
    return "home";
  }*/



}
