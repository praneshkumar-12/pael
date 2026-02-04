package com.example.moneytransfer.service;

import com.example.moneytransfer.domain.dto.TransferRequest;
import com.example.moneytransfer.domain.dto.TransferResponse;
import com.example.moneytransfer.domain.entity.Account;
import com.example.moneytransfer.domain.entity.TransactionLog;
import com.example.moneytransfer.domain.enums.TransactionStatus;
import com.example.moneytransfer.domain.exception.AccountNotActiveException;
import com.example.moneytransfer.domain.exception.AccountNotFoundException;
import com.example.moneytransfer.domain.exception.DuplicateTransferException;
import com.example.moneytransfer.domain.exception.InsufficientBalanceException;
import com.example.moneytransfer.repository.AccountRepository;
import com.example.moneytransfer.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        log.info("Processing transfer request: fromAccountId={}, toAccountId={}, amount={}, idempotencyKey={}",
            request.getFromAccountId(), request.getToAccountId(), request.getAmount(), request.getIdempotencyKey());

        // Check for duplicate transfer using idempotency key
        transactionLogRepository.findByIdempotencyKey(request.getIdempotencyKey())
            .ifPresent(existingTx -> {
                throw new DuplicateTransferException(
                    request.getIdempotencyKey(),
                    existingTx.getId().toString()
                );
            });

        // Validate transfer
        validateTransfer(request);

        // Execute transfer
        return executeTransfer(request);
    }

    private void validateTransfer(TransferRequest request) {
        // Source and destination accounts must differ
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }

        // Both accounts must exist
        Account fromAccount = accountRepository.findById(request.getFromAccountId())
            .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountId()));

        Account toAccount = accountRepository.findById(request.getToAccountId())
            .orElseThrow(() -> new AccountNotFoundException(request.getToAccountId()));

        // Both must be ACTIVE
        if (!fromAccount.isActive()) {
            throw new AccountNotActiveException(request.getFromAccountId());
        }

        if (!toAccount.isActive()) {
            throw new AccountNotActiveException(request.getToAccountId());
        }

        // Amount > 0 (already validated by Bean Validation, but double-check)
        if (request.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // Balance >= amount
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                String.format("Insufficient balance. Current balance: %s, Required: %s",
                    fromAccount.getBalance(), request.getAmount())
            );
        }
    }

    private TransferResponse executeTransfer(TransferRequest request) {
        Account fromAccount = accountRepository.findById(request.getFromAccountId())
            .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountId()));

        Account toAccount = accountRepository.findById(request.getToAccountId())
            .orElseThrow(() -> new AccountNotFoundException(request.getToAccountId()));

        TransactionLog transactionLog = TransactionLog.builder()
            .id(UUID.randomUUID())
            .fromAccountId(request.getFromAccountId())
            .toAccountId(request.getToAccountId())
            .amount(request.getAmount())
            .idempotencyKey(request.getIdempotencyKey())
            .status(TransactionStatus.SUCCESS)
            .build();

        try {
            // Debit first, then credit
            fromAccount.debit(request.getAmount());
            toAccount.credit(request.getAmount());

            // Save accounts
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            // Save transaction log
            transactionLog = transactionLogRepository.save(transactionLog);

            log.info("Transfer completed successfully. Transaction ID: {}", transactionLog.getId());

            return TransferResponse.builder()
                .transactionId(transactionLog.getId().toString())
                .status(TransactionStatus.SUCCESS.name())
                .message("Transfer completed successfully")
                .debitedFrom(request.getFromAccountId())
                .creditedTo(request.getToAccountId())
                .amount(request.getAmount())
                .build();

        } catch (Exception e) {
            // Log failed transaction
            transactionLog.setStatus(TransactionStatus.FAILED);
            transactionLog.setFailureReason(e.getMessage());
            transactionLogRepository.save(transactionLog);

            log.error("Transfer failed. Transaction ID: {}, Error: {}", transactionLog.getId(), e.getMessage());
            throw e;
        }
    }
}
