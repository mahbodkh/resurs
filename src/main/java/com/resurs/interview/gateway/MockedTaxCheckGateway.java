package com.resurs.interview.gateway;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class MockedTaxCheckGateway  implements TaxCheckGateway {
    private final Random random = new Random();

    @Override
    public int getLastYearTax(String socialSecurityNumber) {
        // Simulate some slowness in the response
        try {
            Thread.sleep(random.nextInt(2000) + 500L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return random.nextInt(100000); // Mock tax value between 0 and 100,000
    }
}
