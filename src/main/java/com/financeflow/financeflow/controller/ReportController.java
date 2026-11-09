package com.financeflow.financeflow.controller;

import com.financeflow.financeflow.service.ExcelService;
import com.financeflow.financeflow.service.ExpenseService;
import com.financeflow.financeflow.service.PdfService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;

@Controller
public class ReportController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private com.financeflow.financeflow.service.IncomeService incomeService;

    @Autowired
    private com.financeflow.financeflow.service.ExpenseService expenseService;

    // Report Page
    @GetMapping("/report")
    public String reportPage(Model model, jakarta.servlet.http.HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        model.addAttribute("categories", ExpenseService.CATEGORIES);
        model.addAttribute("currentYear", LocalDate.now().getYear());

        Double totalIncome = incomeService.getTotalIncome(userId);
        Double totalExpense = expenseService.getTotalExpense(userId);

        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("balance", totalIncome - totalExpense);

        java.util.LinkedHashMap<String, Double> categoryTotals = expenseService.getCategoryTotals(userId);
        model.addAttribute("categoryLabels", new java.util.ArrayList<>(categoryTotals.keySet()));
        model.addAttribute("categoryValues", new java.util.ArrayList<>(categoryTotals.values()));

        java.util.LinkedHashMap<String, Double> incomeTrend = incomeService.getMonthlyTrend(userId, 6);
        java.util.LinkedHashMap<String, Double> expenseTrend = expenseService.getMonthlyTrend(userId, 6);

        model.addAttribute("trendLabels", new java.util.ArrayList<>(incomeTrend.keySet()));
        model.addAttribute("trendIncome", new java.util.ArrayList<>(incomeTrend.values()));
        model.addAttribute("trendExpense", new java.util.ArrayList<>(expenseTrend.values()));

        return "report";
    }

    // PDF Report (type = all | monthly | yearly | category)
    @GetMapping("/report/pdf")
    public void exportPdf(@RequestParam(defaultValue = "all") String type,
                           @RequestParam(required = false) String month,
                           @RequestParam(required = false) String year,
                           @RequestParam(required = false) String category,
                           HttpServletResponse response,
                           HttpSession session)
            throws IOException, DocumentException {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=FinanceFlow_Report.pdf"
        );

        pdfService.export(response, userId, type, month, year, category);
    }

    // Excel Report (type = all | monthly | yearly | category)
    @GetMapping("/report/excel")
    public void exportExcel(@RequestParam(defaultValue = "all") String type,
                             @RequestParam(required = false) String month,
                             @RequestParam(required = false) String year,
                             @RequestParam(required = false) String category,
                             HttpServletResponse response,
                             HttpSession session)
            throws IOException {

        Long userId = (Long) session.getAttribute("loggedInUserId");

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=FinanceFlow_Report.xlsx"
        );

        excelService.export(response, userId, type, month, year, category);
    }

}
