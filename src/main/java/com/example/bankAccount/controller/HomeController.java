package com.example.bankAccount.controller;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.Transfer;
import com.example.bankAccount.entity.TransferDate;
import com.example.bankAccount.service.AccountService;
import com.example.bankAccount.service.UserService;
import com.example.bankAccount.util.Round;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

  private final AccountService accountService;

  private final UserService userService;

  public HomeController(AccountService accountService, UserService userService) {
    this.accountService = accountService;
    this.userService = userService;
  }


  @GetMapping("/home")
  public String home (Model model, @RequestParam(required = false) String accountNumber) {

    List<String> listAccountNumbers = userService.getListOfAccountNumbersFromAccountsFromLoggedInUser();
    List<Transfer> listOfTransfers = null;
    List<TransferDate> transferDateList = null;
    Map<String,List<Transfer>> transfersForEachDate = null;
    if (accountNumber != null) {
      Account account = accountService.getAccountByAccountNumber(accountNumber);
      listOfTransfers = accountService.getAccountByAccountNumber(accountNumber).getTransfers();
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
  }
}
