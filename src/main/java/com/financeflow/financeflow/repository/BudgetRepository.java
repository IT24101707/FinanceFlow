package com.financeflow.financeflow.repository;

import com.financeflow.financeflow.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserIdOrderByMonthDesc(Long userId);

    @Query("SELECT COALESCE(MAX(b.budgetAmount), 0) FROM Budget b WHERE b.userId = :userId")
    Double getCurrentBudgetByUser(@Param("userId") Long userId);

}
