package com.example.bankAccount.controller;

import com.example.bankAccount.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
//@RequestMapping("/home")
public class UserController {

private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping("/home")
  public String home () {
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
