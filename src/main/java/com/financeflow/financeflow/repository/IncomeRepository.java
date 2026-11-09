package com.financeflow.financeflow.repository;

import com.financeflow.financeflow.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUserIdOrderByDateDesc(Long userId);

    List<Income> findByUserIdAndSourceContainingIgnoreCase(Long userId, String keyword);

    List<Income> findByUserIdAndDate(Long userId, String date);

    List<Income> findByUserIdAndAmountBetween(Long userId, Double minAmount, Double maxAmount);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.userId = :userId")
    Double getTotalIncomeByUser(@Param("userId") Long userId);

}
