package com.example.bankAccount.service;

import com.example.bankAccount.entity.User;
import com.example.bankAccount.repository.UserRepository;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@org.springframework.stereotype.Service
public class UserService implements UserDetailsService {

  private final UserRepository repository;

  private  final PasswordEncoder passwordEncoder;


  public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  public boolean existsByUsername(String username) {
    if (repository.findByUsername(username) != null) {
      return true;
    } else {
      return false;
  }
  }


  public void saveUserInDatabase (String username, String password) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    repository.save(user);
  }

  //@ModelAttribute("getAllUsers")
  public List<User> getAllUsers () {
    List<User> users = repository.findAll();
    return users;
  }

  public User getUser (String username) {
   return repository.findByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
   User user = repository.findByUsername(username);
   if (user==null) {
     throw new UsernameNotFoundException("No user with username " + username);
   }
   return org.springframework.security.core.userdetails.User
       .withUsername(user.getUsername())
       .password(user.getPassword())
       .build();
  }
}
