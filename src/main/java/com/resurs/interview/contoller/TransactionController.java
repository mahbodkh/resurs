package com.resurs.interview.contoller;

import com.resurs.interview.entity.Transaction;
import com.resurs.interview.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * GET /api/v1/transactions/customer/{customerId}
     * Returns all transactions for a given customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCustomerId(customerId));
    }

    /**
     * GET /api/v1/transactions/customer/{customerId}/average
     * Returns the average transaction amount for a given customer
     */
    @GetMapping("/customer/{customerId}/average")
    public ResponseEntity<Double> getAverageTransactionByCustomerId(@PathVariable Long customerId) {
        double average = transactionService.getAverageTransactionAmount(customerId);
        return ResponseEntity.ok(average);
    }
}
