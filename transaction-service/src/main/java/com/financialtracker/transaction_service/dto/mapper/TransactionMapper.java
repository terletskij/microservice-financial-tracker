package com.financialtracker.transaction_service.dto.mapper;

import com.financialtracker.transaction_service.dto.TransactionDto;
import com.financialtracker.transaction_service.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(TransactionDto dto);
}
