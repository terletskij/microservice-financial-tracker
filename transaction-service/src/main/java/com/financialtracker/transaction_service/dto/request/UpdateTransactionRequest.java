package com.financialtracker.transaction_service.dto.request;

import com.financialtracker.transaction_service.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateTransactionRequest {

    @Schema(description = "Transaction type (INCOME or EXPENSE)", example = "INCOME")
    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @Schema(description = "Transaction money amount", example = "100.00")
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;

    @Schema(description = "Transaction description", example = "Test transaction")
    @NotBlank(message = "Description is required")
    private String description;
}
