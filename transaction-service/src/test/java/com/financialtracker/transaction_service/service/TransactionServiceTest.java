package com.financialtracker.transaction_service.service;

import com.financialtracker.transaction_service.dto.request.CreateTransactionRequest;
import com.financialtracker.transaction_service.dto.request.UpdateTransactionRequest;
import com.financialtracker.transaction_service.entity.Transaction;
import com.financialtracker.transaction_service.enums.TransactionType;
import com.financialtracker.transaction_service.exceptions.ResourceNotFoundException;
import com.financialtracker.transaction_service.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    @DisplayName("getTransactionById returns transaction when it exists")
    void testGetTransactionById_Success() {
        Transaction t = new Transaction();
        t.setId(1L);
        t.setUserId(2L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(t));

        Transaction result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
    @Test
    @DisplayName("getTransactionById throws exception when transaction not found")
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionById(1L));
    }

    @Test
    @DisplayName("getAllTransactionByUserId returns list of existing transactions for the given user")
    void testGetAllTransactionByUserId() {
        Transaction t1 = new Transaction();
        t1.setUserId(1L);
        Transaction t2 = new Transaction();
        t2.setUserId(1L);

        when(transactionRepository.findAllByUserId(1L)).thenReturn(List.of(t1, t2));

        List<Transaction> result = transactionService.getAllTransactionsByUserId(1L);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("createTransaction creates and returns transaction for given user")
    void testCreateTransaction() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setType(TransactionType.EXPENSE);
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Test desc");

        Transaction mappedTransaction = new Transaction();
        mappedTransaction.setType(TransactionType.EXPENSE);
        mappedTransaction.setAmount(new BigDecimal("100.00"));
        mappedTransaction.setDescription("Test desc");

        when(modelMapper.map(request, Transaction.class)).thenReturn(mappedTransaction);

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        Transaction result = transactionService.createTransaction(request, 1L);

        assertNotNull(result.getId(), "Transaction ID should be generated");
        assertEquals(1L, result.getUserId(), "User ID should be set corrected");
        assertEquals(new BigDecimal("100.00"), result.getAmount());

        verify(modelMapper).map(request, Transaction.class);
        verify(transactionRepository).save(mappedTransaction);
    }

    @Test
    @DisplayName("deleteTransactionById deletes transaction if it exists")
    void testDeleteTransactionById_Success() {
        Transaction t = new Transaction();
        t.setId(1L);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(t));

        transactionService.deleteTransactionById(1L);

        verify(transactionRepository).delete(t);
    }

    @Test
    @DisplayName("deleteTransactionById throws exception if transaction not found")
    void testDeleteTransactionById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.deleteTransactionById(1L));
    }

    @Test
    @DisplayName("updateTransactionById updates transaction by its id if transaction exists")
    void testUpdateTransactionById_Success() {
        Transaction t = new Transaction();
        t.setId(1L);

        UpdateTransactionRequest request = new UpdateTransactionRequest();
        request.setType(TransactionType.EXPENSE);
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Test desc");

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(t));
        Transaction result = transactionService.updateTransactionById(1L, request);

        assertEquals(new BigDecimal("100.00"), t.getAmount());
        assertEquals("Test desc", t.getDescription());
        assertEquals(TransactionType.EXPENSE, t.getType());
        verify(transactionRepository).save(t);
    }

    @Test
    @DisplayName("updateTransactionById throws exception if transaction not found")
    void testUpdateTransactionById_NotFound() {
        UpdateTransactionRequest request = new UpdateTransactionRequest();
        request.setType(TransactionType.EXPENSE);

        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> transactionService.updateTransactionById(1L, request));
    }
}
