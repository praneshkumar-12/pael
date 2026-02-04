package com.example.moneytransfer.service;

import com.example.moneytransfer.domain.dto.AccountResponse;
import com.example.moneytransfer.domain.entity.Account;
import com.example.moneytransfer.domain.entity.TransactionLog;
import com.example.moneytransfer.domain.exception.AccountNotFoundException;
import com.example.moneytransfer.repository.AccountRepository;
import com.example.moneytransfer.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException(id));
        
        return AccountResponse.builder()
            .id(account.getId())
            .holderName(account.getHolderName())
            .balance(account.getBalance())
            .status(account.getStatus().name())
            .build();
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException(id));
        
        return account.getBalance();
    }

    @Transactional(readOnly = true)
    public List<TransactionLog> getTransactions(Long accountId) {
        // Verify account exists
        accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));
        
        // Get all transactions where account is either sender or receiver
        return transactionLogRepository.findByAccountId(accountId);
    }
}
