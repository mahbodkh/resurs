package com.resurs.interview.gateway;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountBalanceResponse {
    private String accountId;
    private double balance;
    private String currency;
    private LocalDateTime lastUpdated;

    @Override
    public String toString() {
        return "AccountBalanceResponse{" +
                "accountId='" + accountId + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
