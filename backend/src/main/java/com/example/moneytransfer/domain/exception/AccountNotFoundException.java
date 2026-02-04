package com.example.moneytransfer.domain.exception;

public class AccountNotFoundException extends RuntimeException {
    
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(Long accountId) {
        super(String.format("Account with ID %d not found", accountId));
    }
}
