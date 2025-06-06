package com.financialtracker.transaction_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financialtracker.transaction_service.dto.request.CreateTransactionRequest;
import com.financialtracker.transaction_service.dto.request.UpdateTransactionRequest;
import com.financialtracker.transaction_service.enums.TransactionType;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(2L, null, null)
        );
    }

    @Test
    @DisplayName("POST [201 created] /transactions - Create transaction successfully")
    void testCreateTransaction() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setType(TransactionType.EXPENSE);
        request.setAmount(new BigDecimal("100.23"));
        request.setDescription("Test desc");

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Transaction successfully created"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.userId").value(2))
                .andExpect(jsonPath("$.data.type").value("EXPENSE"))
                .andExpect(jsonPath("$.data.amount").value(100.23));
    }

    @Test
    @DisplayName("POST [400 BadRequest] - returns 400 for invalid input")
    void testCreateTransaction_InvalidInput() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setDescription("Missing required fields");

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET [200 OK] /transactions/{id} - retrieves a transaction successfully")
    void testGetTransactionById_Success() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setType(TransactionType.EXPENSE);
        request.setAmount(new BigDecimal("100.23"));
        request.setDescription("Test desc");

        MvcResult creationResult = mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = creationResult.getResponse().getContentAsString();
        int id = JsonPath.read(responseContent, "$.data.id");

        mockMvc.perform(get("/transactions/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction successfully received"))
                .andExpect(jsonPath("$.data.id").value(id));
    }

    @Test
    @DisplayName("GET [404 NotFound] /transactions/{id} - returns 404 when transaction not found")
    void testGetTransactionById_NotFound() throws Exception {
        mockMvc.perform(get("/transactions/777"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction not found"));
    }

    @Test
    @DisplayName("GET [401 Unauthorized] /transactions - Returns 401 when no user ID is provided")
    void testGetAllTransactions_Unauthorized() throws Exception {
        SecurityContextHolder.clearContext();

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET [200 OK] /transactions - Retrieves all transactions successfully")
    void testGetAllTransactions_Success() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setType(TransactionType.EXPENSE);
        request.setAmount(new BigDecimal("100.23"));
        request.setDescription("Test desc");

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All transactions successfully received"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("PUT [200 OK] /transactions/{id} - Updates a transaction successfully")
    void testUpdateTransaction_Success() throws Exception {
        CreateTransactionRequest createRequest = new CreateTransactionRequest();
        createRequest.setType(TransactionType.EXPENSE);
        createRequest.setAmount(new BigDecimal("100.23"));
        createRequest.setDescription("Test desc");

        MvcResult creationResult = mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = creationResult.getResponse().getContentAsString();
        int id = JsonPath.read(responseContent, "$.data.id");

        UpdateTransactionRequest request = new UpdateTransactionRequest();
        request.setType(TransactionType.INCOME);
        request.setAmount(new BigDecimal("123.23"));
        request.setDescription("Test update");

        mockMvc.perform(put("/transactions/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction update success"));
    }

    @Test
    @DisplayName("PUT [404 NotFound] /transactions/{id} - returns 404 when updating non-existing transaction")
    void testUpdateTransaction_NotFound() throws Exception {
        UpdateTransactionRequest request = new UpdateTransactionRequest();
        request.setType(TransactionType.EXPENSE);
        request.setAmount(new BigDecimal("100.23"));
        request.setDescription("Test update");

        mockMvc.perform(put("/transactions/777")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction not found"));
    }

    @Test
    @DisplayName("DELETE [204 NoContent] /transactions/{id} - Deletes a transaction successfully")
    void testDeleteTransaction_Success() throws Exception {
        CreateTransactionRequest createRequest = new CreateTransactionRequest();
        createRequest.setType(TransactionType.EXPENSE);
        createRequest.setAmount(new BigDecimal("100.23"));
        createRequest.setDescription("Test desc");

        MvcResult creationResult = mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = creationResult.getResponse().getContentAsString();
        int id = JsonPath.read(responseContent, "$.data.id");

        mockMvc.perform(delete("/transactions/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE [404 NotFound] /transactions/{id} - Returns 404 when deleting non-existing transaction")
    void testDeleteTransaction_NotFound() throws Exception {
        mockMvc.perform(delete("/transactions/777"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction not found"));
    }
}