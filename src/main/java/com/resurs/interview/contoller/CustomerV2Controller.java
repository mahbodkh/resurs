package com.resurs.interview.contoller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.resurs.interview.entity.Customer;
import com.resurs.interview.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/customers")
public class CustomerV2Controller {

    private final CustomerService customerService;

    /**
     * POST /api/v2/customers
     * Creates a new customer
     */
    @PostMapping()
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer created = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /api/v2/customers/{customerId}
     * Returns a customer by ID
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * GET /api/v2/customers/{customerId}/score
     * Returns the credit score for a given customer
     */
    @GetMapping("/{customerId}/score")
    public ResponseEntity<Double> getCreditScore(@PathVariable Long customerId) {
        return customerService.getCreditScore(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * POST /api/v2/customers/{customerId}/loan
     * Requests a loan for a given customer
     */
    @PostMapping("/{customerId}/loan")
    public ResponseEntity<String> requestLoan(@PathVariable Long customerId) {
        boolean approved = customerService.requestLoan(customerId);
        return ResponseEntity.ok(approved ? "Loan approved" : "Loan denied");
    }
}
