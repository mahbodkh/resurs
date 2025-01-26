package com.resurs.interview.contoller;

import com.resurs.interview.entity.Customer;
import com.resurs.interview.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CustomerV1Controller {
    private final CustomerService customerService;

    /**
     * POST /api/v1/customer
     * Creates a new customer
     */
    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer created = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /api/v1/customer/{customerId}
     * Returns a customer by ID
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * GET /api/v1/customers/{customerId}/creditScore
     * Returns the credit score for a given customer
     */
    @GetMapping("/customers/{customerId}/creditScore")
    public ResponseEntity<Double> getCreditScore(@PathVariable Long customerId) {
        return customerService.getCreditScore(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * POST /api/v1/customers/{customerId}/loan
     * Requests a loan for a given customer
     */
    @PostMapping("/customers/{customerId}/loan")
    public ResponseEntity<String> requestLoan(@PathVariable Long customerId) {
        boolean approved = customerService.requestLoan(customerId);
        return ResponseEntity.ok(approved ? "Loan approved" : "Loan denied");
    }
}
