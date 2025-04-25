package com.financialtracker.transaction_service.service;

import com.financialtracker.transaction_service.dto.TransactionDto;
import com.financialtracker.transaction_service.dto.request.CreateTransactionRequest;
import com.financialtracker.transaction_service.dto.request.UpdateTransactionRequest;
import com.financialtracker.transaction_service.entity.Transaction;
import com.financialtracker.transaction_service.exceptions.ResourceNotFoundException;
import com.financialtracker.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public Transaction getTransactionById(Long id, Long userId) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        if (!transaction.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to this transaction");
        }
        return transaction;
    }

    @Override
    public List<Transaction> getAllTransactionsByUserId(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    @Override
    public Transaction createTransaction(CreateTransactionRequest request, Long userId) {
        Transaction transaction = modelMapper.map(request, Transaction.class);
        transaction.setUserId(userId);
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransactionById(Long id, Long userId) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        if (!transaction.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to this transaction");
        }
        transactionRepository.delete(transaction);
    }

    @Override
    public Transaction updateTransactionById(Long id, Long userId, UpdateTransactionRequest request) {
        Transaction transaction = getTransactionById(id, userId);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        return transactionRepository.save(transaction);
    }

    @Override
    public TransactionDto convertToDto(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDto.class);
    }

    @Override
    public Transaction convertFromDto(TransactionDto dto) {
        return modelMapper.map(dto, Transaction.class);
    }

    @Override
    public List<TransactionDto> convertToDtos(List<Transaction> transactions) {
        return transactions.stream().map(this::convertToDto).toList();
    }

    @Override
    public List<Transaction> convertFromDtos(List<TransactionDto> dtos) {
        return dtos.stream().map(this::convertFromDto).toList();
    }
}