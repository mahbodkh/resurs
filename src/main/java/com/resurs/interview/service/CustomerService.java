package com.resurs.interview.service;

import java.util.Optional;

import com.resurs.interview.entity.Customer;
import com.resurs.interview.gateway.TaxCheckGateway;
import com.resurs.interview.gateway.ThirdPartyAPIService;
import com.resurs.interview.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final double BALANCE_WEIGHT = 0.5;
    private static final double TRANSACTION_AMOUNT_WEIGHT = 0.3;
    private static final double TRANSACTION_COUNT_WEIGHT = 0.2;
    private static final double NORMALIZATION_FACTOR = 1000.0;

    private final CustomerRepository customerRepository;
    private final TransactionService transactionService;
    private final ThirdPartyAPIService thirdPartyAPIService;
    private final TaxCheckGateway taxCheckGateway;


    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    public Optional<Double> getCreditScore(Long customerId) {
        return customerRepository.findById(customerId).map(customer -> {
            final double newScore = calculateCreditScore(customerId);
            customer.setCreditScore(newScore);
            customerRepository.save(customer);
            return newScore;
        });
    }

    /**
     * Requests a loan for a given customer
     *
     * @param customerId the ID of the customer.
     * @return true if the loan was granted, false otherwise.
     */
    public boolean requestLoan(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {

                    final double currentScore = calculateCreditScore(customerId);
                    final int lastYearTax = taxCheckGateway.getLastYearTax(customer.getSocialSecurityNumber());

                    return isLoanEligible(lastYearTax, currentScore);
                })
                .orElse(false);
    }


    /**
     * Determines if a loan should be granted based on the last year tax and the credit score.
     *
     * @param lastYearTax the last year tax.
     * @param creditScore the credit score.
     * @return true if the loan should be granted, false otherwise.
     */
    private boolean isLoanEligible(int lastYearTax, double creditScore) {
        return (creditScore >= 70.00) && (lastYearTax > 20_000);
    }


    /**
     * Calculates the credit score for a given customer.
     *
     * @param customerId the ID of the customer.
     * @return the credit score.
     */
    private double calculateCreditScore(Long customerId) {
        final double accountBalance = thirdPartyAPIService
                .getCustomerAccountBalance("account_externalId_" + customerId)
                .getBalance();

        double normalizedBalance = accountBalance / NORMALIZATION_FACTOR;

        final double averageTransactionAmount = transactionService.getAverageTransactionAmount(customerId);
        final int transactionCount = transactionService.getTransactionsByCustomerId(customerId).size();
//        double creditRisk = getCheckCreditRisk(customerId);
//        double loanHistory = getCheckLoanHistory(customerId);



        double rawScore = (normalizedBalance * BALANCE_WEIGHT)
                + (averageTransactionAmount * TRANSACTION_AMOUNT_WEIGHT)
                + (transactionCount * TRANSACTION_COUNT_WEIGHT);

        // normalize the score to be between 0 and 100
        rawScore = Math.max(0, Math.min(100, rawScore));

        return Math.round(rawScore);
    }
}
