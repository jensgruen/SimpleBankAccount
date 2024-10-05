package com.example.bankAccount.controller;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.User;
import com.example.bankAccount.repository.UserRepository;
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
//@RequestMapping("/home")
public class UserController {

private final UserService service;

private final UserRepository userRepository;

  public UserController(UserService service, UserRepository userRepository) {
    this.service = service;
    this.userRepository = userRepository;
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
  public String home (Model model) {
    String username = getLoggedInUser();
    User user = userRepository.findByUsername(username);
  //  List<Account> accounts = new ArrayList<>();
//    Account account1 = new Account();
//    account1.setUser(user);
//    account1.setAccountNumber("12334");
//    account1.setAccountType("Checking");
//    account1.setBalance(100);
//    accounts.add(account1);
//
//    Account account2 = new Account();
//    account2.setUser(user);
//    account2.setAccountNumber("123345");
//    account2.setAccountType("Checking");
//    account2.setBalance(100);
//    accounts.add(account2);


  //  user.setAccounts(accounts);

    List<Account> userAccounts = user.getAccounts();

    List<String> users = new ArrayList<>();
    for (int i=0; i< userAccounts.size(); i++) {
      users.add(userAccounts.get(i).getAccountNumber());
    }
    System.out.println(users);
    //List<String> users = List.of();
    model.addAttribute("users", users);
    return "home";
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
