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

}
