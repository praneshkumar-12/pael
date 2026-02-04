package com.example.moneytransfer.domain.entity;

import com.example.moneytransfer.domain.enums.AccountStatus;
import com.example.moneytransfer.domain.exception.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
            .id(1L)
            .holderName("John Doe")
            .balance(new BigDecimal("1000.00"))
            .status(AccountStatus.ACTIVE)
            .version(0)
            .build();
    }

    @Test
    void testDebitSuccess() {
        BigDecimal amount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("500.00");

        account.debit(amount);

        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    void testDebitInsufficientBalance() {
        BigDecimal amount = new BigDecimal("1500.00");

        assertThrows(InsufficientBalanceException.class, () -> {
            account.debit(amount);
        });

        // Balance should remain unchanged
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    void testDebitZeroAmount() {
        BigDecimal amount = BigDecimal.ZERO;

        assertThrows(IllegalArgumentException.class, () -> {
            account.debit(amount);
        });
    }

    @Test
    void testDebitNegativeAmount() {
        BigDecimal amount = new BigDecimal("-100.00");

        assertThrows(IllegalArgumentException.class, () -> {
            account.debit(amount);
        });
    }

    @Test
    void testCreditSuccess() {
        BigDecimal amount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("1500.00");

        account.credit(amount);

        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    void testCreditZeroAmount() {
        BigDecimal amount = BigDecimal.ZERO;

        assertThrows(IllegalArgumentException.class, () -> {
            account.credit(amount);
        });
    }

    @Test
    void testCreditNegativeAmount() {
        BigDecimal amount = new BigDecimal("-100.00");

        assertThrows(IllegalArgumentException.class, () -> {
            account.credit(amount);
        });
    }

    @Test
    void testIsActive() {
        account.setStatus(AccountStatus.ACTIVE);
        assertTrue(account.isActive());

        account.setStatus(AccountStatus.LOCKED);
        assertFalse(account.isActive());

        account.setStatus(AccountStatus.CLOSED);
        assertFalse(account.isActive());
    }
}
