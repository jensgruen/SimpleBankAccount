package com.example.bankAccount.repository;

import com.example.bankAccount.entity.Account;
import com.example.bankAccount.entity.TransferDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferDateRepository extends JpaRepository<TransferDate,Integer> {

  TransferDate findByTransferDate(String transferDate);
}
