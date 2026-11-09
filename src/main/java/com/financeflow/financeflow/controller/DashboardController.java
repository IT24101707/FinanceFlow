package com.financeflow.financeflow.controller;

import com.financeflow.financeflow.dto.TransactionView;
import com.financeflow.financeflow.entity.Expense;
import com.financeflow.financeflow.entity.Income;
import com.financeflow.financeflow.service.BudgetService;
import com.financeflow.financeflow.service.ExpenseService;
import com.financeflow.financeflow.service.IncomeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        String userName = (String) session.getAttribute("loggedInUserName");

        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String previousMonth = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // ===== Totals (all time) =====
        Double totalIncome = incomeService.getTotalIncome(userId);
        Double totalExpense = expenseService.getTotalExpense(userId);
        Double balance = totalIncome - totalExpense;

        // ===== Monthly Expense =====
        Double monthlyExpense = expenseService.getMonthlyExpense(userId, currentMonth);

        // ===== Top Spending Category =====
        String topCategory = expenseService.getTopSpendingCategory(userId);

        // ===== Month-over-month comparisons =====
        double curMonthIncome = incomeService.getMonthlyIncome(userId, currentMonth);
        double prevMonthIncome = incomeService.getMonthlyIncome(userId, previousMonth);

        double curMonthExpense = expenseService.getMonthlyExpense(userId, currentMonth);
        double prevMonthExpense = expenseService.getMonthlyExpense(userId, previousMonth);

        double curMonthBalance = curMonthIncome - curMonthExpense;
        double prevMonthBalance = prevMonthIncome - prevMonthExpense;

        double incomeChange = percentChange(curMonthIncome, prevMonthIncome);
        double expenseChange = percentChange(curMonthExpense, prevMonthExpense);
        double balanceChange = percentChange(curMonthBalance, prevMonthBalance);

        double curSavingsRate = curMonthIncome > 0 ? (curMonthBalance / curMonthIncome) * 100 : 0;
        double prevSavingsRate = prevMonthIncome > 0 ? (prevMonthBalance / prevMonthIncome) * 100 : 0;
        double savingsRateChange = curSavingsRate - prevSavingsRate;

        // ===== Budget =====
        Double budget = budgetService.getCurrentBudget(userId);
        Double remainingBudget = budget - totalExpense;

        double progress = 0;
        if (budget > 0) {
            progress = (totalExpense / budget) * 100;
            if (progress > 100) {
                progress = 100;
            }
        }

        // ===== Category breakdown (for donut chart) =====
        LinkedHashMap<String, Double> categoryTotals = expenseService.getCategoryTotals(userId);

        // ===== Recent transactions (combined income + expense, latest 6) =====
        List<TransactionView> recentTransactions = new ArrayList<>();

        for (Income i : incomeService.getAllIncome(userId)) {
            recentTransactions.add(new TransactionView(i.getDate(), i.getSource(), i.getSource(), "Income", i.getAmount()));
        }

        for (Expense e : expenseService.getAllExpense(userId)) {
            recentTransactions.add(new TransactionView(e.getDate(), e.getCategory(), e.getCategory(), "Expense", e.getAmount()));
        }

        recentTransactions.sort(Comparator.comparing(TransactionView::getDate, Comparator.nullsLast(Comparator.reverseOrder())));

        if (recentTransactions.size() > 6) {
            recentTransactions = recentTransactions.subList(0, 6);
        }

        // ===== Model =====
        model.addAttribute("userName", userName);
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("balance", balance);
        model.addAttribute("monthlyExpense", monthlyExpense);
        model.addAttribute("topCategory", topCategory);

        model.addAttribute("incomeChange", incomeChange);
        model.addAttribute("expenseChange", expenseChange);
        model.addAttribute("balanceChange", balanceChange);
        model.addAttribute("savingsRate", curSavingsRate);
        model.addAttribute("savingsRateChange", savingsRateChange);

        model.addAttribute("incomeChart", totalIncome);
        model.addAttribute("expenseChart", totalExpense);

        model.addAttribute("budget", budget);
        model.addAttribute("remainingBudget", remainingBudget);
        model.addAttribute("progress", progress);

        model.addAttribute("today", LocalDate.now());

        model.addAttribute("categoryLabels", new ArrayList<>(categoryTotals.keySet()));
        model.addAttribute("categoryValues", new ArrayList<>(categoryTotals.values()));

        model.addAttribute("recentTransactions", recentTransactions);

        return "dashboard";
    }

    private double percentChange(double current, double previous) {

        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }

        return ((current - previous) / previous) * 100.0;
    }

}
