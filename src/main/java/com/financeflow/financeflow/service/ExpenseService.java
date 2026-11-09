package com.financeflow.financeflow.service;

import com.financeflow.financeflow.entity.Expense;
import com.financeflow.financeflow.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Fixed set of expense categories used across the app
    public static final List<String> CATEGORIES = Arrays.asList(
            "Food", "Transport", "Rent", "Bills",
            "Education", "Entertainment", "Shopping", "Healthcare", "Other"
    );

    public Expense saveExpense(Expense expense, Long userId) {
        expense.setUserId(userId);
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpense(Long userId) {
        return expenseRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Expense getExpenseById(Long id, Long userId) {

        Optional<Expense> expense = expenseRepository.findById(id);

        if (expense.isPresent() && expense.get().getUserId() != null
                && expense.get().getUserId().equals(userId)) {
            return expense.get();
        }

        return null;
    }

    public void deleteExpense(Long id, Long userId) {
        Expense expense = getExpenseById(id, userId);
        if (expense != null) {
            expenseRepository.deleteById(id);
        }
    }

    public List<Expense> searchExpense(Long userId, String keyword) {
        return expenseRepository.findByUserIdAndCategoryContainingIgnoreCase(userId, keyword);
    }

    public List<Expense> filterByDate(Long userId, String date) {
        return expenseRepository.findByUserIdAndDate(userId, date);
    }

    public List<Expense> filterByCategory(Long userId, String category) {
        return expenseRepository.findByUserIdAndCategory(userId, category);
    }

    public List<Expense> filterByAmountRange(Long userId, Double min, Double max) {
        return expenseRepository.findByUserIdAndAmountBetween(userId, min, max);
    }

    public Double getTotalExpense(Long userId) {
        Double total = expenseRepository.getTotalExpenseByUser(userId);
        return total == null ? 0.0 : total;
    }

    // Total expense for the current month (monthPrefix format: "yyyy-MM")
    public Double getMonthlyExpense(Long userId, String monthPrefix) {

        double total = 0.0;

        for (Expense e : getAllExpense(userId)) {
            if (e.getDate() != null && e.getDate().startsWith(monthPrefix)) {
                total += e.getAmount();
            }
        }

        return total;
    }

    // Category with the highest total spend
    public String getTopSpendingCategory(Long userId) {

        Map<String, Double> totals = new HashMap<>();

        for (Expense e : getAllExpense(userId)) {
            totals.merge(e.getCategory(), e.getAmount(), Double::sum);
        }

        return totals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    // All category totals, sorted highest spend first (used for pie/donut charts)
    public java.util.LinkedHashMap<String, Double> getCategoryTotals(Long userId) {

        Map<String, Double> totals = new HashMap<>();

        for (Expense e : getAllExpense(userId)) {
            totals.merge(e.getCategory(), e.getAmount(), Double::sum);
        }

        java.util.LinkedHashMap<String, Double> sorted = new java.util.LinkedHashMap<>();

        totals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> sorted.put(entry.getKey(), entry.getValue()));

        return sorted;
    }

    // Expense totals for the last N months, oldest to newest, keyed by "yyyy-MM"
    public java.util.LinkedHashMap<String, Double> getMonthlyTrend(Long userId, int months) {

        java.util.LinkedHashMap<String, Double> trend = new java.util.LinkedHashMap<>();

        for (int i = months - 1; i >= 0; i--) {
            String monthKey = java.time.LocalDate.now().minusMonths(i)
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
            trend.put(monthKey, getMonthlyExpense(userId, monthKey));
        }

        return trend;
    }

}
