package com.example.moneytransfer.controller;

import com.example.moneytransfer.domain.dto.AccountResponse;
import com.example.moneytransfer.domain.entity.TransactionLog;
import com.example.moneytransfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        AccountResponse response = accountService.getAccount(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        BigDecimal balance = accountService.getBalance(id);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionLog>> getTransactions(@PathVariable Long id) {
        List<TransactionLog> transactions = accountService.getTransactions(id);
        return ResponseEntity.ok(transactions);
    }
}
