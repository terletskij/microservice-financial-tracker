package com.financialtracker.transaction_service.service;

import com.financialtracker.transaction_service.dto.TransactionDto;
import com.financialtracker.transaction_service.dto.mapper.TransactionMapper;
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
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionDto getTransactionById(Long id, Long userId) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        if (!transaction.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to this transaction");
        }
        TransactionDto dto = transactionMapper.toDto(transaction);

        return dto;
    }

    @Override
    public List<TransactionDto> getAllTransactionsByUserId(Long userId) {
        List<TransactionDto> dtos = transactionRepository.findAllByUserId(userId)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
        return dtos;
    }

    @Override
    public TransactionDto createTransaction(CreateTransactionRequest request, Long userId) {
        Transaction transaction = modelMapper.map(request, Transaction.class);
        transaction.setUserId(userId);
        TransactionDto dto = transactionMapper.toDto(transaction);

        transactionRepository.save(transaction);
        return dto;
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
    public TransactionDto updateTransactionById(Long id, Long userId, UpdateTransactionRequest request) {
        Transaction transaction = transactionMapper.toEntity(getTransactionById(id, userId));
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);
    }
}