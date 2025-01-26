package com.resurs.interview.gateway;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.*;

import com.resurs.interview.exception.ExternalServiceException;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MockedThirdPartyApiService implements ThirdPartyAPIService {

    private final Random random = new Random();
    private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    @Override
    public AccountBalanceResponse getCustomerAccountBalance(String accountId) {
        try {

            return singleThreadExecutor.submit(() -> {

                        long delay = 500 + random.nextInt(1000);
                        Thread.sleep(delay);

                        // example of a call to an external service
//                        String url = "external-service-example.com/api/account/" + accountId;
//                        ResponseEntity<AccountBalanceResponse> response = restTemplate.getForEntity(url, AccountBalanceResponse.class);
//                        if (response.getStatusCode().is2xxSuccessful()) {}

                        return ResponseEntity.ok(
                                        new AccountBalanceResponse(accountId,
                                                random.nextInt(100000),
                                                "SEK",
                                                LocalDateTime.now().minusMinutes(10)
                                        ))
                                .getBody();

                    })
                    .get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ExternalServiceException("Thread was interrupted", e);
        } catch (ExecutionException e) {
            throw new ExternalServiceException("Error from external service callable", e);
        }
    }

    @PreDestroy
    public void shutdownExecutor() {
        singleThreadExecutor.shutdown();
        try {
            if (!singleThreadExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                singleThreadExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            singleThreadExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
