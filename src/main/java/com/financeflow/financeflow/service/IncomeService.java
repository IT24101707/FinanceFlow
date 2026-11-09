package com.financeflow.financeflow.service;

import com.financeflow.financeflow.entity.Income;
import com.financeflow.financeflow.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    public Income saveIncome(Income income, Long userId) {
        income.setUserId(userId);
        return incomeRepository.save(income);
    }

    public List<Income> getAllIncome(Long userId) {
        return incomeRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Income getIncomeById(Long id, Long userId) {

        Optional<Income> income = incomeRepository.findById(id);

        if (income.isPresent() && income.get().getUserId() != null
                && income.get().getUserId().equals(userId)) {
            return income.get();
        }

        return null;
    }

    public void deleteIncome(Long id, Long userId) {
        Income income = getIncomeById(id, userId);
        if (income != null) {
            incomeRepository.deleteById(id);
        }
    }

    public List<Income> searchIncome(Long userId, String keyword) {
        return incomeRepository.findByUserIdAndSourceContainingIgnoreCase(userId, keyword);
    }

    public List<Income> filterByDate(Long userId, String date) {
        return incomeRepository.findByUserIdAndDate(userId, date);
    }

    public List<Income> filterByAmountRange(Long userId, Double min, Double max) {
        return incomeRepository.findByUserIdAndAmountBetween(userId, min, max);
    }

    public Double getTotalIncome(Long userId) {
        Double total = incomeRepository.getTotalIncomeByUser(userId);
        return total == null ? 0.0 : total;
    }

    // Total income for a given "yyyy-MM" month prefix
    public Double getMonthlyIncome(Long userId, String monthPrefix) {

        double total = 0.0;

        for (Income i : getAllIncome(userId)) {
            if (i.getDate() != null && i.getDate().startsWith(monthPrefix)) {
                total += i.getAmount();
            }
        }

        return total;
    }

    // Income totals for the last N months, oldest to newest, keyed by "yyyy-MM"
    public java.util.LinkedHashMap<String, Double> getMonthlyTrend(Long userId, int months) {

        java.util.LinkedHashMap<String, Double> trend = new java.util.LinkedHashMap<>();

        for (int i = months - 1; i >= 0; i--) {
            String monthKey = java.time.LocalDate.now().minusMonths(i)
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
            trend.put(monthKey, getMonthlyIncome(userId, monthKey));
        }

        return trend;
    }


}
