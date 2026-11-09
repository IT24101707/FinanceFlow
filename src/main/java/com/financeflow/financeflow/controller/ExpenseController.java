package com.financeflow.financeflow.controller;

import com.financeflow.financeflow.entity.Expense;
import com.financeflow.financeflow.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public String expensePage(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("expense", new Expense());
        model.addAttribute("expenseList", expenseService.getAllExpense(userId));
        model.addAttribute("categories", ExpenseService.CATEGORIES);
        model.addAttribute("totalExpense", expenseService.getTotalExpense(userId));

        return "expense";
    }

    @PostMapping("/save")
    public String saveExpense(@ModelAttribute Expense expense, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        expenseService.saveExpense(expense, userId);

        return "redirect:/expense";
    }

    @GetMapping("/edit/{id}")
    public String editExpense(@PathVariable Long id, Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("expense", expenseService.getExpenseById(id, userId));
        model.addAttribute("expenseList", expenseService.getAllExpense(userId));
        model.addAttribute("categories", ExpenseService.CATEGORIES);
        model.addAttribute("totalExpense", expenseService.getTotalExpense(userId));

        return "expense";
    }

    @PostMapping("/update")
    public String updateExpense(@ModelAttribute Expense expense, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        expenseService.saveExpense(expense, userId);

        return "redirect:/expense";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        expenseService.deleteExpense(id, userId);

        return "redirect:/expense";
    }

    @GetMapping("/search")
    public String searchExpense(@RequestParam String keyword, Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("expense", new Expense());
        model.addAttribute("expenseList", expenseService.searchExpense(userId, keyword));
        model.addAttribute("categories", ExpenseService.CATEGORIES);
        model.addAttribute("totalExpense", expenseService.getTotalExpense(userId));

        return "expense";
    }

    @GetMapping("/filter")
    public String filterExpense(@RequestParam String date, Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("expense", new Expense());
        model.addAttribute("expenseList", expenseService.filterByDate(userId, date));
        model.addAttribute("categories", ExpenseService.CATEGORIES);
        model.addAttribute("totalExpense", expenseService.getTotalExpense(userId));

        return "expense";
    }

    @GetMapping("/filter-category")
    public String filterByCategory(@RequestParam String category, Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("expense", new Expense());
        model.addAttribute("expenseList", expenseService.filterByCategory(userId, category));
        model.addAttribute("categories", ExpenseService.CATEGORIES);
        model.addAttribute("totalExpense", expenseService.getTotalExpense(userId));

        return "expense";
    }

    @GetMapping("/filter-amount")
    public String filterByAmount(@RequestParam(required = false) Double min,
                                  @RequestParam(required = false) Double max,
                                  Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        double minVal = (min != null) ? min : 0.0;
        double maxVal = (max != null) ? max : Double.MAX_VALUE;

        model.addAttribute("expense", new Expense());
        model.addAttribute("expenseList", expenseService.filterByAmountRange(userId, minVal, maxVal));
        model.addAttribute("categories", ExpenseService.CATEGORIES);
        model.addAttribute("totalExpense", expenseService.getTotalExpense(userId));

        return "expense";
    }

}
