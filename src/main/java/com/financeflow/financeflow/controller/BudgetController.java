package com.financeflow.financeflow.controller;

import com.financeflow.financeflow.entity.Budget;
import com.financeflow.financeflow.service.BudgetService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public String budgetPage(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("budget", new Budget());
        model.addAttribute("budgetList", budgetService.getAllBudget(userId));

        return "budget";
    }

    @PostMapping("/save")
    public String saveBudget(@ModelAttribute Budget budget, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        budgetService.saveBudget(budget, userId);

        return "redirect:/budget";
    }

    @GetMapping("/edit/{id}")
    public String editBudget(@PathVariable Long id, Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("budget", budgetService.getBudget(id, userId));
        model.addAttribute("budgetList", budgetService.getAllBudget(userId));

        return "budget";
    }

    @PostMapping("/update")
    public String updateBudget(@ModelAttribute Budget budget, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        budgetService.saveBudget(budget, userId);

        return "redirect:/budget";
    }

    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Long id, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        budgetService.deleteBudget(id, userId);

        return "redirect:/budget";
    }

}
