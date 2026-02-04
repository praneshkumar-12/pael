package com.example.moneytransfer.domain.entity;

import com.example.moneytransfer.domain.enums.AccountStatus;
import com.example.moneytransfer.domain.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @Column(name = "balance", nullable = false, precision = 18, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @PreUpdate
    @PrePersist
    protected void updateTimestamp() {
        this.lastUpdated = Instant.now();
    }

    /**
     * Debits the specified amount from this account.
     * 
     * @param amount the amount to debit (must be positive)
     * @throws InsufficientBalanceException if the account balance is less than the amount
     * @throws IllegalArgumentException if the amount is not positive
     */
    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                String.format("Insufficient balance. Current balance: %s, Required: %s", 
                    this.balance, amount)
            );
        }
        
        this.balance = this.balance.subtract(amount);
    }

    /**
     * Credits the specified amount to this account.
     * 
     * @param amount the amount to credit (must be positive)
     * @throws IllegalArgumentException if the amount is not positive
     */
    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        
        this.balance = this.balance.add(amount);
    }

    /**
     * Checks if the account is active.
     * 
     * @return true if the account status is ACTIVE, false otherwise
     */
    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(this.status);
    }
}
