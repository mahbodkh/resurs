package com.resurs.interview.service;

import com.resurs.interview.entity.Transaction;
import com.resurs.interview.entity.TransactionType;
import com.resurs.interview.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionsByCustomerId() {
        // given
        final LocalDateTime localDateTime = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        Transaction transaction = new Transaction();
        transaction.setCustomerId(1L);
        transaction.setAmount(100.0);
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setTransactionDate(localDateTime);

        // when
        when(transactionRepository.findByCustomerId(1L)).thenReturn(List.of(transaction));

        // then
        List<Transaction> transactions = transactionService.getTransactionsByCustomerId(1L);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(100.0, transactions.get(0).getAmount());
        assertEquals(localDateTime, transactions.get(0).getTransactionDate());
        assertEquals(TransactionType.DEBIT, transactions.get(0).getTransactionType());
        verify(transactionRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void testGetAverageTransactionAmount() {
        // given
        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(1L);
        transaction1.setAmount(100.0);

        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(1L);
        transaction2.setAmount(200.0);

        // when
        when(transactionRepository.findByCustomerId(1L)).thenReturn(List.of(transaction1, transaction2));

        // then
        double averageAmount = transactionService.getAverageTransactionAmount(1L);

        assertEquals(150.0, averageAmount);
        verify(transactionRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void testGetAverageTransactionAmount_noTransactions() {
        // when
        when(transactionRepository.findByCustomerId(1L)).thenReturn(Collections.emptyList());

        // then
        double averageAmount = transactionService.getAverageTransactionAmount(1L);

        assertEquals(0.0, averageAmount);
        verify(transactionRepository, times(1)).findByCustomerId(1L);
    }
}