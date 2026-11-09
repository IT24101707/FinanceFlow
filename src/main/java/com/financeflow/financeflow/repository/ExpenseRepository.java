package com.financeflow.financeflow.repository;

import com.financeflow.financeflow.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserIdOrderByDateDesc(Long userId);

    List<Expense> findByUserIdAndCategoryContainingIgnoreCase(Long userId, String keyword);

    List<Expense> findByUserIdAndCategory(Long userId, String category);

    List<Expense> findByUserIdAndDate(Long userId, String date);

    List<Expense> findByUserIdAndAmountBetween(Long userId, Double minAmount, Double maxAmount);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.userId = :userId")
    Double getTotalExpenseByUser(@Param("userId") Long userId);

}
