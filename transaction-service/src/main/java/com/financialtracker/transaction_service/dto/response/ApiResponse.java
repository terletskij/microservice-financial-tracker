package com.financialtracker.transaction_service.dto.response;

import lombok.Data;

@Data
public class ApiResponse {
    private String message;
    private Object data;
}
