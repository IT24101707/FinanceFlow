package com.financeflow.financeflow.controller;

import com.financeflow.financeflow.entity.Income;
import com.financeflow.financeflow.service.IncomeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @GetMapping
    public String incomePage(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("income", new Income());
        model.addAttribute("incomeList", incomeService.getAllIncome(userId));
        model.addAttribute("totalIncome", incomeService.getTotalIncome(userId));

        return "income";
    }

    @PostMapping("/save")
    public String saveIncome(@ModelAttribute Income income, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        incomeService.saveIncome(income, userId);

        return "redirect:/income";
    }

    @GetMapping("/edit/{id}")
    public String editIncome(@PathVariable Long id, Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("income", incomeService.getIncomeById(id, userId));
        model.addAttribute("incomeList", incomeService.getAllIncome(userId));
        model.addAttribute("totalIncome", incomeService.getTotalIncome(userId));

        return "income";
    }

    @PostMapping("/update")
    public String updateIncome(@ModelAttribute Income income, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        incomeService.saveIncome(income, userId);

        return "redirect:/income";
    }

    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Long id, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");
        incomeService.deleteIncome(id, userId);

        return "redirect:/income";
    }

    @GetMapping("/search")
    public String searchIncome(@RequestParam String keyword, Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("income", new Income());
        model.addAttribute("incomeList", incomeService.searchIncome(userId, keyword));
        model.addAttribute("totalIncome", incomeService.getTotalIncome(userId));

        return "income";
    }

    @GetMapping("/filter")
    public String filterIncome(@RequestParam String date, Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("income", new Income());
        model.addAttribute("incomeList", incomeService.filterByDate(userId, date));
        model.addAttribute("totalIncome", incomeService.getTotalIncome(userId));

        return "income";
    }

    @GetMapping("/filter-amount")
    public String filterByAmount(@RequestParam(required = false) Double min,
                                  @RequestParam(required = false) Double max,
                                  Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        double minVal = (min != null) ? min : 0.0;
        double maxVal = (max != null) ? max : Double.MAX_VALUE;

        model.addAttribute("income", new Income());
        model.addAttribute("incomeList", incomeService.filterByAmountRange(userId, minVal, maxVal));
        model.addAttribute("totalIncome", incomeService.getTotalIncome(userId));

        return "income";
    }

}
