package org.example.mockjaxrs2.app3;

import org.springframework.stereotype.Service;

@Service
public class ExpensiveService {

    public String computeSomethingExpensive() {
        throw new RuntimeException("Too expensive to compute in a unitest");
    }
}
