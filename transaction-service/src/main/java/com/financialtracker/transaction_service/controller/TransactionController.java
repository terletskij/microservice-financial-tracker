package com.financialtracker.transaction_service.controller;

import com.financialtracker.transaction_service.dto.TransactionDto;
import com.financialtracker.transaction_service.dto.request.CreateTransactionRequest;
import com.financialtracker.transaction_service.dto.request.UpdateTransactionRequest;
import com.financialtracker.transaction_service.dto.response.ApiResponse;
import com.financialtracker.transaction_service.entity.Transaction;
import com.financialtracker.transaction_service.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Transactions", description = "Endpoints for managing transactions")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Transaction successfully created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @Operation(summary = "Create transaction", description = "Creates a new transaction for the user")
    @PostMapping
    public ResponseEntity<ApiResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = transactionService.createTransaction(request, userId);
        TransactionDto transactionDto = transactionService.convertToDto(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Transaction successfully created", transactionDto));
    }

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction successfully retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @Operation(summary = "Get transaction by ID", description = "Retrieves a transaction by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTransactionById(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TransactionDto transactionDto = transactionService.convertToDto(transactionService.getTransactionById(id, userId));
        return ResponseEntity.ok(new ApiResponse("Transaction successfully received", transactionDto));
    }

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions successfully retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @Operation(summary = "Get all transactions by user", description = "Retrieves all transactions by user ID")
    @GetMapping
    public ResponseEntity<ApiResponse> getAllTransactions() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Transaction> transactions = transactionService.getAllTransactionsByUserId(userId);
        List<TransactionDto> dtos = transactionService.convertToDtos(transactions);
        return ResponseEntity.ok(new ApiResponse("All transactions successfully received", dtos));
    }

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction successfully updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @Operation(summary = "Update transaction", description = "Updates the entire transaction by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTransaction(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateTransactionRequest request) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = transactionService.updateTransactionById(id, userId, request);
        TransactionDto dto = transactionService.convertToDto(transaction);
        return ResponseEntity.ok(new ApiResponse("Transaction update success", dto));
    }

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Transaction successfully deleted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @Operation(summary = "Delete transaction", description = "Removes transaction by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transactionService.deleteTransactionById(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}