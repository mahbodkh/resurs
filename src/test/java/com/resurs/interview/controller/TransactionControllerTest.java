package com.resurs.interview.controller;

import com.resurs.interview.contoller.TransactionController;
import com.resurs.interview.entity.Transaction;
import com.resurs.interview.entity.TransactionType;
import com.resurs.interview.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void testGetTransactionsByCustomerId() throws Exception {
        // given
        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(1001L);
        transaction1.setAmount(500.00);

        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 1, 15, 10, 30, 0);
        transaction1.setTransactionDate(localDateTime1);

        TransactionType debitType = TransactionType.DEBIT;
        transaction1.setTransactionType(debitType);


        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(1001L);
        transaction2.setAmount(1200.50);

        LocalDateTime localDateTime2 = LocalDateTime.of(2023, 2, 20, 14, 15, 0);
        transaction2.setTransactionDate(localDateTime2);

        TransactionType creditType = TransactionType.CREDIT;
        transaction2.setTransactionType(creditType);

        // when
        when(transactionService.getTransactionsByCustomerId(1001L)).thenReturn(List.of(transaction1, transaction2));

        // then
        mockMvc.perform(get("/api/v1/transactions/customer/1001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1001L))
                .andExpect(jsonPath("$[0].amount").value(500.00))
                .andExpect(jsonPath("$[0].transactionDate").value("2023-01-15T10:30:00"))
                .andExpect(jsonPath("$[0].transactionType").value(debitType.toString()))

                .andExpect(jsonPath("$[1].customerId").value(1001L))
                .andExpect(jsonPath("$[1].amount").value(1200.50))
                .andExpect(jsonPath("$[1].transactionDate").value("2023-02-20T14:15:00"))
                .andExpect(jsonPath("$[1].transactionType").value(creditType.toString()));
    }

    @Test
    void testGetAverageTransactionByCustomerId() throws Exception {
        // when
        when(transactionService.getAverageTransactionAmount(1001L)).thenReturn(675.25
        );

        // then
        mockMvc.perform(get("/api/v1/transactions/customer/1001/average")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("675.25"));
    }
}