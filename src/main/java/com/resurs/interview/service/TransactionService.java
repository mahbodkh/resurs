package com.resurs.interview.service;

import java.util.List;
import com.resurs.interview.entity.Transaction;
import com.resurs.interview.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {


    private final TransactionRepository transactionRepository;

    /**
     * Get all transactions for a specific customer.
     * @param customerId the ID of the customer.
     * @return list of transactions for that customer.
     */
    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    /**
     * Get the average transaction amount for the customer.
     * @param customerId the ID of the customer.
     * @return average transaction amount.
     */
    public double getAverageTransactionAmount(Long customerId) {
        List<Transaction> transactions = getTransactionsByCustomerId(customerId);
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .average()
                .orElse(0.0);
    }
}
