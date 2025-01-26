package com.resurs.interview.repository;

import java.util.Optional;
import com.resurs.interview.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findBySocialSecurityNumber(String ssn);
}
