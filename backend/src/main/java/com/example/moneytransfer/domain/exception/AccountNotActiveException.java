package com.example.moneytransfer.domain.exception;

public class AccountNotActiveException extends RuntimeException {
    
    public AccountNotActiveException(String message) {
        super(message);
    }

    public AccountNotActiveException(Long accountId) {
        super(String.format("Account with ID %d is not active", accountId));
    }
}
