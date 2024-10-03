package com.example.bankAccount.repository;

import com.example.bankAccount.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User,Integer> {
  User findByUsername(String username);



}
