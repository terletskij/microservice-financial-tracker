package com.financialtracker.transaction_service.repository;

import com.financialtracker.transaction_service.entity.Transaction;
import com.financialtracker.transaction_service.enums.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @DisplayName("findAllByUserId returns all transactions for the given user")
    void testFindAllByUserId() {
        Transaction transaction1 = new Transaction();
        transaction1.setUserId(1L);
        transaction1.setType(TransactionType.EXPENSE);
        transaction1.setAmount(new BigDecimal("100.00"));
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setUserId(1L);
        transaction2.setType(TransactionType.EXPENSE);
        transaction2.setAmount(new BigDecimal("100.00"));
        transactionRepository.save(transaction2);

        List<Transaction> user1transactions = transactionRepository.findAllByUserId(1L);

        assertThat(user1transactions).hasSize(2);
    }

    @Test
    @DisplayName("findAllByUserId returns empty list if no transactions exist for the given user")
    void testFindAllByUserId_NoTransactions() {
        List<Transaction> transactions = transactionRepository.findAllByUserId(999L);
        assertThat(transactions).isEmpty();
    }
}
