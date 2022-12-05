package com.ebay.tests.jersey.mock_bean;

import org.springframework.stereotype.Service;

@Service
public class ExpensiveService {

    public String computeSomethingExpensive() {
        throw new RuntimeException("Too expensive to compute in a unitest");
    }
}
