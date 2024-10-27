package com.example.bankAccount.repository;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {


}
