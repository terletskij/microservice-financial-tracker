package com.financialtracker.transaction_service.service;

import com.financialtracker.transaction_service.dto.TransactionDto;
import com.financialtracker.transaction_service.dto.request.CreateTransactionRequest;
import com.financialtracker.transaction_service.dto.request.UpdateTransactionRequest;
import com.financialtracker.transaction_service.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction getTransactionById(Long id, Long userId);

    List<Transaction> getAllTransactionsByUserId(Long userId);

    Transaction createTransaction(CreateTransactionRequest request, Long userId);

    void deleteTransactionById(Long id, Long userId);

    Transaction updateTransactionById(Long id, Long userId, UpdateTransactionRequest request);

    TransactionDto convertToDto(Transaction transaction);

    Transaction convertFromDto(TransactionDto dto);

    List<TransactionDto> convertToDtos(List<Transaction> transactions);

    List<Transaction> convertFromDtos(List<TransactionDto> dtos);
}
