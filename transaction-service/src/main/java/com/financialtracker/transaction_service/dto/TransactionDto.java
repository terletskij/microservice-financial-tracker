package com.financialtracker.transaction_service.dto;

import com.financialtracker.transaction_service.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {

    private Long id;

    private TransactionType type;

    private BigDecimal amount;

    private String description;

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}