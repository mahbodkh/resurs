package com.resurs.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resurs.interview.contoller.CustomerV1Controller;
import com.resurs.interview.entity.Customer;
import com.resurs.interview.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerV1Controller.class)
class CustomerV1ControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;


    private final static String SSN = "19900101-1234";

    @Test
    void testCreateCustomer() throws Exception {
        Customer input = new Customer();
        input.setName("Ebrahim K");
        input.setSocialSecurityNumber(SSN);

        Customer saved = new Customer();
        saved.setId(1001L);
        saved.setName("Ebrahim K");
        saved.setSocialSecurityNumber(SSN);

        when(customerService.createCustomer(any(Customer.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1001L))
                .andExpect(jsonPath("$.name").value("Ebrahim K"))
                .andExpect(jsonPath("$.socialSecurityNumber").value(SSN));
    }

    @Test
    void testGetCustomer_found() throws Exception {
        Customer existing = new Customer();
        existing.setId(1001L);
        existing.setName("Ebrahim K");

        when(customerService.getCustomerById(1001L)).thenReturn(Optional.of(existing));

        mockMvc.perform(get("/api/v1/customer/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1001L))
                .andExpect(jsonPath("$.name").value("Ebrahim K"));
    }

    @Test
    void testGetCustomer_notFound() throws Exception {
        when(customerService.getCustomerById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/customers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCreditScore_found() throws Exception {
        when(customerService.getCreditScore(1001L)).thenReturn(Optional.of(78.5));

        mockMvc.perform(get("/api/v1/customers/1001/creditScore"))
                .andExpect(status().isOk())
                .andExpect(content().string("78.5"));
    }

    @Test
    void testGetCreditScore_notFound() throws Exception {
        when(customerService.getCreditScore(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/customer/999/creditScore"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRequestLoan_approved() throws Exception {
        when(customerService.requestLoan(10L)).thenReturn(true);

        mockMvc.perform(post("/api/v1/customers/10/loan"))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan approved"));
    }

    @Test
    void testRequestLoan_denied() throws Exception {
        when(customerService.requestLoan(10L)).thenReturn(false);

        mockMvc.perform(post("/api/v1/customers/10/loan"))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan denied"));
    }
}
