package com.resurs.interview.gateway;

public interface ThirdPartyAPIService {
    AccountBalanceResponse getCustomerAccountBalance(String accountId);
}
