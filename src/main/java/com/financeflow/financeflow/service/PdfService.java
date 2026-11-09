package com.financeflow.financeflow.service;

import com.financeflow.financeflow.entity.Expense;
import com.financeflow.financeflow.entity.Income;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfService {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    public void export(HttpServletResponse response, Long userId,
                        String type, String month, String year, String category)
            throws IOException, DocumentException {

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        Paragraph title = new Paragraph("FinanceFlow Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph(getReportLabel(type, month, year, category));
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);

        document.add(new Paragraph(" "));

        List<Income> incomes = filterIncomes(incomeService.getAllIncome(userId), type, month, year);
        List<Expense> expenses = filterExpenses(expenseService.getAllExpense(userId), type, month, year, category);

        document.add(new Paragraph("========== INCOME ==========", sectionFont));

        for (Income income : incomes) {
            document.add(new Paragraph(
                    income.getDate() + " | " +
                            income.getSource() + " | Rs. " +
                            income.getAmount()
            ));
        }

        document.add(new Paragraph(" "));

        document.add(new Paragraph("========== EXPENSE ==========", sectionFont));

        for (Expense expense : expenses) {
            document.add(new Paragraph(
                    expense.getDate() + " | " +
                            expense.getCategory() + " | Rs. " +
                            expense.getAmount()
            ));
        }

        document.add(new Paragraph(" "));

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

        document.add(new Paragraph("--------------------------------"));
        document.add(new Paragraph("Total Income : Rs. " + totalIncome));
        document.add(new Paragraph("Total Expense : Rs. " + totalExpense));
        document.add(new Paragraph("Balance : Rs. " + (totalIncome - totalExpense)));

        document.close();
    }

    private List<Income> filterIncomes(List<Income> all, String type, String month, String year) {

        if ("monthly".equalsIgnoreCase(type) && month != null && !month.isEmpty()) {
            return all.stream()
                    .filter(i -> i.getDate() != null && i.getDate().startsWith(month))
                    .collect(Collectors.toList());
        }

        if ("yearly".equalsIgnoreCase(type) && year != null && !year.isEmpty()) {
            return all.stream()
                    .filter(i -> i.getDate() != null && i.getDate().startsWith(year))
                    .collect(Collectors.toList());
        }

        return all;
    }

    private List<Expense> filterExpenses(List<Expense> all, String type, String month, String year, String category) {

        if ("monthly".equalsIgnoreCase(type) && month != null && !month.isEmpty()) {
            return all.stream()
                    .filter(e -> e.getDate() != null && e.getDate().startsWith(month))
                    .collect(Collectors.toList());
        }

        if ("yearly".equalsIgnoreCase(type) && year != null && !year.isEmpty()) {
            return all.stream()
                    .filter(e -> e.getDate() != null && e.getDate().startsWith(year))
                    .collect(Collectors.toList());
        }

        if ("category".equalsIgnoreCase(type) && category != null && !category.isEmpty()) {
            return all.stream()
                    .filter(e -> category.equalsIgnoreCase(e.getCategory()))
                    .collect(Collectors.toList());
        }

        return all;
    }

    private String getReportLabel(String type, String month, String year, String category) {

        if ("monthly".equalsIgnoreCase(type)) {
            return "Monthly Report - " + (month != null ? month : "");
        }

        if ("yearly".equalsIgnoreCase(type)) {
            return "Yearly Report - " + (year != null ? year : "");
        }

        if ("category".equalsIgnoreCase(type)) {
            return "Category Report - " + (category != null ? category : "");
        }

        return "Full Report";
    }

}
