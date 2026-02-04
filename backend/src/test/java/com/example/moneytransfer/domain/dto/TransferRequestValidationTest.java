package com.example.moneytransfer.domain.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransferRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRequest() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1L);
        request.setToAccountId(2L);
        request.setAmount(new BigDecimal("100.00"));
        request.setIdempotencyKey("test-key-123");

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullFields() {
        TransferRequest request = new TransferRequest();

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);
        assertEquals(4, violations.size()); // All 4 fields are null

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fromAccountId")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("toAccountId")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idempotencyKey")));
    }

    @Test
    void testInvalidAmount() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1L);
        request.setToAccountId(2L);
        request.setAmount(new BigDecimal("0.00")); // Less than 0.01
        request.setIdempotencyKey("test-key-123");

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }

    @Test
    void testNegativeAmount() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1L);
        request.setToAccountId(2L);
        request.setAmount(new BigDecimal("-100.00"));
        request.setIdempotencyKey("test-key-123");

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("amount")));
    }

    @Test
    void testNullFromAccountId() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(null);
        request.setToAccountId(2L);
        request.setAmount(new BigDecimal("100.00"));
        request.setIdempotencyKey("test-key-123");

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fromAccountId")));
    }

    @Test
    void testNullToAccountId() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1L);
        request.setToAccountId(null);
        request.setAmount(new BigDecimal("100.00"));
        request.setIdempotencyKey("test-key-123");

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("toAccountId")));
    }

    @Test
    void testNullIdempotencyKey() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1L);
        request.setToAccountId(2L);
        request.setAmount(new BigDecimal("100.00"));
        request.setIdempotencyKey(null);

        Set<ConstraintViolation<TransferRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("idempotencyKey")));
    }
}
