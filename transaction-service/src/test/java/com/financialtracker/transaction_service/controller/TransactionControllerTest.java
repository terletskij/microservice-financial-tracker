package com.financialtracker.transaction_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financialtracker.transaction_service.dto.TransactionDto;
import com.financialtracker.transaction_service.dto.request.CreateTransactionRequest;
import com.financialtracker.transaction_service.entity.Transaction;
import com.financialtracker.transaction_service.enums.TransactionType;
import com.financialtracker.transaction_service.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    @DisplayName("POST /transactions - Create transaction successfully")
    void testCreateTransaction() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setType(TransactionType.EXPENSE);
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Test desc");

        Transaction transaction = new Transaction();

        transaction.setId(1L);
        transaction.setType(TransactionType.EXPENSE);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDescription("Test desc");
        transaction.setUserId(2L);

        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setUserId(transaction.getUserId());

        Mockito.when(transactionService.createTransaction(any(CreateTransactionRequest.class), eq(2L)))
                .thenReturn(transaction);
        Mockito.when(transactionService.convertToDto(any(Transaction.class))).thenReturn(dto);
        mockMvc.perform(post("/transactions")
                .header("X-User-Id", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Transaction successfully created"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.userId").value(2))
                .andExpect(jsonPath("$.data.type").value("EXPENSE"))
                .andExpect(jsonPath("$.data.amount").value(new BigDecimal("100.00")));
    }

    // todo: fix BigDecimal serialization (must be 100.00, not 100.0)
}
