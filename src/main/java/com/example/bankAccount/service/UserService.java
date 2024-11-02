package com.example.bankAccount.service;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.User;
import com.example.bankAccount.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@org.springframework.stereotype.Service
public class UserService implements UserDetailsService {

  private final UserRepository repository;

  private  final PasswordEncoder passwordEncoder;


  public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  public boolean doesUserExits(String username) {
    return repository.findByUsername(username) != null;
  }


  public void saveUserInDatabase (String username, String password) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    repository.save(user);
  }


  public User getUser (String username) {
   return repository.findByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
   User user = getUser(username);
   if (user==null) {
     throw new UsernameNotFoundException("No user with username " + username);
   }
   return org.springframework.security.core.userdetails.User
       .withUsername(user.getUsername())
       .password(user.getPassword())
       .build();
  }

  public String getLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName();
  }


  public List<Account> listAccountsFromLoggedInUser() {
    String username = getLoggedInUser();
    User user = getUser(username);
    return user.getAccounts();
  }


  public List<String> getListOfAccountNumbersFromAccountsFromLoggedInUser () {
    List<Account> accountsOfLoggedInUser = listAccountsFromLoggedInUser();
    List<String> accounts = new ArrayList<>();
    for (Account account : accountsOfLoggedInUser) {
      accounts.add(account.getAccountNumber());
    }
    return accounts;
    }


  }

