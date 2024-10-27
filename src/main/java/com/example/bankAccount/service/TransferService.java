package com.example.bankAccount.service;


import com.example.bankAccount.entity.Transfer;
import com.example.bankAccount.repository.TransferRepository;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

  private final TransferRepository transferRepository;


  public TransferService(TransferRepository transferRepository) {
    this.transferRepository = transferRepository;
  }

  public Transfer saveTransferToDatabase (Transfer transfer) {
    return transferRepository.save(transfer);
  }

}
