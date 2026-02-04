package com.example.moneytransfer.domain.exception;

public class DuplicateTransferException extends RuntimeException {
    
    public DuplicateTransferException(String message) {
        super(message);
    }

    public DuplicateTransferException(String idempotencyKey, String transactionId) {
        super(String.format("Duplicate transfer detected. Idempotency key: %s, Transaction ID: %s", 
            idempotencyKey, transactionId));
    }
}
