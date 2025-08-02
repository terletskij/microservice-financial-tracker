package com.financialtracker.transaction_service.service;

import com.financialtracker.transaction_service.dto.TransactionDto;
import com.financialtracker.transaction_service.dto.request.CreateTransactionRequest;
import com.financialtracker.transaction_service.dto.request.UpdateTransactionRequest;
import com.financialtracker.transaction_service.entity.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionDto getTransactionById(Long id, Long userId);

    List<TransactionDto> getAllTransactionsByUserId(Long userId);

    TransactionDto createTransaction(CreateTransactionRequest request, Long userId);

    void deleteTransactionById(Long id, Long userId);

    TransactionDto updateTransactionById(Long id, Long userId, UpdateTransactionRequest request);
}