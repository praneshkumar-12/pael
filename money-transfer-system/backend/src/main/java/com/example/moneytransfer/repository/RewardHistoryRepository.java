package com.example.moneytransfer.repository;

import com.example.moneytransfer.domain.entity.RewardHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RewardHistoryRepository extends JpaRepository<RewardHistory, UUID> {

    List<RewardHistory> findByUserIdOrderByCreatedOnDesc(Long userId);

    boolean existsByTransactionId(UUID transactionId);

    @Query("select coalesce(sum(r.pointsEarned), 0) from RewardHistory r where r.userId = :userId")
    Integer sumPointsEarnedByUserId(@Param("userId") Long userId);
}
