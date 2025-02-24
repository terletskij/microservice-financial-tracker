package com.financialtracker.transaction_service.service;

import com.financialtracker.transaction_service.dto.TransactionDto;
import com.financialtracker.transaction_service.dto.request.CreateTransactionRequest;
import com.financialtracker.transaction_service.dto.request.UpdateTransactionRequest;
import com.financialtracker.transaction_service.entity.Transaction;
import com.financialtracker.transaction_service.exceptions.ResourceNotFoundException;
import com.financialtracker.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    @Override
    public List<Transaction> getAllTransactionsByUserId(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    @Override
    public Transaction createTransaction(CreateTransactionRequest request, Long userId) {
        Transaction transaction = new Transaction();
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setUserId(userId);

        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction updateTransactionById(Long id, UpdateTransactionRequest request) {
        Transaction transaction = getTransactionById(id);
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
        Transaction transaction = new Transaction();
        transaction.setUserId(dto.getUserId());
        transaction.setId(dto.getId());
        transaction.setType(dto.getType());
        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());
        transaction.setCreatedAt(dto.getCreatedAt());
        transaction.setUpdatedAt(dto.getUpdatedAt());
        return transaction;
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