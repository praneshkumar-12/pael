package com.example.moneytransfer.repository;

import com.example.moneytransfer.domain.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {
    
    Optional<TransactionLog> findByIdempotencyKey(String idempotencyKey);
    
    @Query("SELECT t FROM TransactionLog t WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId ORDER BY t.createdOn DESC")
    List<TransactionLog> findByAccountId(@Param("accountId") Long accountId);
}
