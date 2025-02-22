package com.financialtracker.transaction_service.dto.request;

import com.financialtracker.transaction_service.enums.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateTransactionRequest {

    private TransactionType type;

    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;

    private String description;
}
