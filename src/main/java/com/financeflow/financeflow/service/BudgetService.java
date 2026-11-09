package com.financeflow.financeflow.service;

import com.financeflow.financeflow.entity.Budget;
import com.financeflow.financeflow.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public Budget saveBudget(Budget budget, Long userId) {
        budget.setUserId(userId);
        return budgetRepository.save(budget);
    }

    public List<Budget> getAllBudget(Long userId) {
        return budgetRepository.findByUserIdOrderByMonthDesc(userId);
    }

    public Budget getBudget(Long id, Long userId) {

        Budget budget = budgetRepository.findById(id).orElse(null);

        if (budget != null && budget.getUserId() != null && budget.getUserId().equals(userId)) {
            return budget;
        }

        return null;
    }

    public void deleteBudget(Long id, Long userId) {

        Budget budget = getBudget(id, userId);

        if (budget != null) {
            budgetRepository.deleteById(id);
        }
    }

    public Double getCurrentBudget(Long userId) {
        Double budget = budgetRepository.getCurrentBudgetByUser(userId);
        return budget == null ? 0.0 : budget;
    }

}
