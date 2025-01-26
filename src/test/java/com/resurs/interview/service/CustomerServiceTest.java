package com.resurs.interview.service;

import com.resurs.interview.entity.Customer;
import com.resurs.interview.gateway.AccountBalanceResponse;
import com.resurs.interview.gateway.TaxCheckGateway;
import com.resurs.interview.gateway.ThirdPartyAPIService;
import com.resurs.interview.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TransactionService transactionService;
    @Mock
    private ThirdPartyAPIService thirdPartyAPIService;
    @Mock
    private TaxCheckGateway taxCheckGateway;

    @InjectMocks
    private CustomerService customerService;

    private final static String SSN = "19900101-1234";
    private final static String NAME = "Ebrahim K";

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setName(NAME);
        customer.setSocialSecurityNumber(SSN);

        // Stub repository save
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer created = customerService.createCustomer(customer);

        assertNotNull(created);
        assertEquals(NAME, created.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById_found() {
        Customer customer = new Customer();
        customer.setId(1001L);
        customer.setName(NAME);

        when(customerRepository.findById(1001L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerById(1001L);

        assertTrue(result.isPresent());
        assertEquals(NAME, result.get().getName());
        verify(customerRepository, times(1)).findById(1001L);
    }


    @Test
    void testGetCustomerById_notFound() {
        // when
        when(customerRepository.findById(1001L)).thenReturn(Optional.empty());
        Optional<Customer> result = customerService.getCustomerById(1001L);

        // then
        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(1001L);
    }


    @Test
    void testGetCreditScore() {
        // given
        Customer customer = new Customer();
        customer.setId(1001L);
        customer.setSocialSecurityNumber(SSN);

        // when
        when(customerRepository.findById(1001L)).thenReturn(Optional.of(customer));
        when(thirdPartyAPIService.getCustomerAccountBalance("account_externalId_1001"))
                .thenReturn(new AccountBalanceResponse("account_externalId_1001", 5000.0, "SEK", null));

        when(transactionService.getAverageTransactionAmount(1001L)).thenReturn(250.0);
        when(transactionService.getTransactionsByCustomerId(1001L))
                .thenReturn(Collections.nCopies(3, null));

        // then
        Optional<Double> scoreOpt = customerService.getCreditScore(1001L);
        assertTrue(scoreOpt.isPresent(), "Score should be present");

        double score = scoreOpt.get();
        assertEquals(78.00, score);

        verify(customerRepository).save(customer);
        assertEquals(78.00, customer.getCreditScore());
    }

    @Test
    void testRequestLoan_approved() {
        // given
        Customer customer = new Customer();
        customer.setId(1001L);
        customer.setSocialSecurityNumber(SSN);

        // when
        when(customerRepository.findById(1001L)).thenReturn(Optional.of(customer));
        //
        when(thirdPartyAPIService.getCustomerAccountBalance("account_externalId_1001"))
                .thenReturn(new AccountBalanceResponse("account_externalId_1001", 7000.0, "SEK", null));
        when(transactionService.getAverageTransactionAmount(1001L)).thenReturn(500.0);
        when(transactionService.getTransactionsByCustomerId(1001L))
                .thenReturn(Collections.nCopies(2, null)); // 2 transactions

        //
        when(taxCheckGateway.getLastYearTax(SSN)).thenReturn(30000);

        // then
        boolean approved = customerService.requestLoan(1001L);
        assertTrue(approved, "Loan should be approved under these conditions.");
    }

    @Test
    void testRequestLoan_deniedBecauseOfLowScore() {
        // given
        Customer customer = new Customer();
        customer.setId(1001L);
        customer.setSocialSecurityNumber(SSN);

        // when
        when(customerRepository.findById(1001L)).thenReturn(Optional.of(customer));
        //
        when(thirdPartyAPIService.getCustomerAccountBalance("account_externalId_1001"))
                .thenReturn(new AccountBalanceResponse("account_externalId_1001", 500.0, "SEK", null));
        when(transactionService.getAverageTransactionAmount(1001L)).thenReturn(10.0);
        when(transactionService.getTransactionsByCustomerId(1001L))
                .thenReturn(Collections.emptyList());
        //
        when(taxCheckGateway.getLastYearTax(SSN)).thenReturn(30000);

        // then
        boolean approved = customerService.requestLoan(1001L);
        assertFalse(approved, "Loan should be denied because the score is too low.");
    }
}
