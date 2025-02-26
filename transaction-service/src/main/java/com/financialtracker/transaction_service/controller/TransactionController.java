package com.financialtracker.transaction_service.controller;

import com.financialtracker.transaction_service.dto.TransactionDto;
import com.financialtracker.transaction_service.dto.request.CreateTransactionRequest;
import com.financialtracker.transaction_service.dto.request.UpdateTransactionRequest;
import com.financialtracker.transaction_service.dto.response.ApiResponse;
import com.financialtracker.transaction_service.entity.Transaction;
import com.financialtracker.transaction_service.exceptions.ResourceNotFoundException;
import com.financialtracker.transaction_service.service.TransactionService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    //todo: after auth-service + API gateway fix endpoints with user verification

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse> createTransaction(@RequestBody CreateTransactionRequest request,
                                                         @RequestHeader("X-User-Id") Long userId) {
        Transaction transaction = transactionService.createTransaction(request, userId);
        TransactionDto transactionDto = transactionService.convertToDto(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Transaction successfully created", transactionDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTransactionById(@PathVariable Long id) {
        TransactionDto transactionDto = transactionService.convertToDto(transactionService.getTransactionById(id));
        return ResponseEntity.ok(new ApiResponse("Transaction successfully received", transactionDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTransactions(@RequestHeader("X-User-Id") Long userId) {
        List<Transaction> transactions = transactionService.getAllTransactionsByUserId(userId);
        List<TransactionDto> dtos = transactionService.convertToDtos(transactions);
        return ResponseEntity.ok(new ApiResponse("All transactions successfully received", dtos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTransaction(@PathVariable Long id, @RequestBody UpdateTransactionRequest request) {
        Transaction transaction = transactionService.updateTransactionById(id, request);
        TransactionDto dto = transactionService.convertToDto(transaction);
        return ResponseEntity.ok(new ApiResponse("Transaction update success", dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransactionById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}