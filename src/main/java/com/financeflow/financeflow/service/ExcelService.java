package com.financeflow.financeflow.service;

import com.financeflow.financeflow.entity.Expense;
import com.financeflow.financeflow.entity.Income;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelService {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    public void export(HttpServletResponse response, Long userId,
                        String type, String month, String year, String category) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        List<Income> incomes = filterIncomes(incomeService.getAllIncome(userId), type, month, year);
        List<Expense> expenses = filterExpenses(expenseService.getAllExpense(userId), type, month, year, category);

        Sheet incomeSheet = workbook.createSheet("Income");

        Row incomeHeader = incomeSheet.createRow(0);
        incomeHeader.createCell(0).setCellValue("ID");
        incomeHeader.createCell(1).setCellValue("Source");
        incomeHeader.createCell(2).setCellValue("Amount");
        incomeHeader.createCell(3).setCellValue("Date");

        int rowCount = 1;

        for (Income income : incomes) {

            Row row = incomeSheet.createRow(rowCount++);

            row.createCell(0).setCellValue(income.getId());
            row.createCell(1).setCellValue(income.getSource());
            row.createCell(2).setCellValue(income.getAmount());
            row.createCell(3).setCellValue(income.getDate());

        }

        Sheet expenseSheet = workbook.createSheet("Expense");

        Row expenseHeader = expenseSheet.createRow(0);
        expenseHeader.createCell(0).setCellValue("ID");
        expenseHeader.createCell(1).setCellValue("Category");
        expenseHeader.createCell(2).setCellValue("Amount");
        expenseHeader.createCell(3).setCellValue("Date");

        rowCount = 1;

        for (Expense expense : expenses) {

            Row row = expenseSheet.createRow(rowCount++);

            row.createCell(0).setCellValue(expense.getId());
            row.createCell(1).setCellValue(expense.getCategory());
            row.createCell(2).setCellValue(expense.getAmount());
            row.createCell(3).setCellValue(expense.getDate());

        }

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

        Sheet summarySheet = workbook.createSheet("Summary");

        Row r1 = summarySheet.createRow(0);
        r1.createCell(0).setCellValue("Total Income");
        r1.createCell(1).setCellValue(totalIncome);

        Row r2 = summarySheet.createRow(1);
        r2.createCell(0).setCellValue("Total Expense");
        r2.createCell(1).setCellValue(totalExpense);

        Row r3 = summarySheet.createRow(2);
        r3.createCell(0).setCellValue("Balance");
        r3.createCell(1).setCellValue(totalIncome - totalExpense);

        incomeSheet.autoSizeColumn(0);
        incomeSheet.autoSizeColumn(1);
        incomeSheet.autoSizeColumn(2);
        incomeSheet.autoSizeColumn(3);

        expenseSheet.autoSizeColumn(0);
        expenseSheet.autoSizeColumn(1);
        expenseSheet.autoSizeColumn(2);
        expenseSheet.autoSizeColumn(3);

        summarySheet.autoSizeColumn(0);
        summarySheet.autoSizeColumn(1);

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);

        workbook.close();

        outputStream.close();
    }

    private List<Income> filterIncomes(List<Income> all, String type, String month, String year) {

        if ("monthly".equalsIgnoreCase(type) && month != null && !month.isEmpty()) {
            return all.stream().filter(i -> i.getDate() != null && i.getDate().startsWith(month))
                    .collect(Collectors.toList());
        }

        if ("yearly".equalsIgnoreCase(type) && year != null && !year.isEmpty()) {
            return all.stream().filter(i -> i.getDate() != null && i.getDate().startsWith(year))
                    .collect(Collectors.toList());
        }

        return all;
    }

    private List<Expense> filterExpenses(List<Expense> all, String type, String month, String year, String category) {

        if ("monthly".equalsIgnoreCase(type) && month != null && !month.isEmpty()) {
            return all.stream().filter(e -> e.getDate() != null && e.getDate().startsWith(month))
                    .collect(Collectors.toList());
        }

        if ("yearly".equalsIgnoreCase(type) && year != null && !year.isEmpty()) {
            return all.stream().filter(e -> e.getDate() != null && e.getDate().startsWith(year))
                    .collect(Collectors.toList());
        }

        if ("category".equalsIgnoreCase(type) && category != null && !category.isEmpty()) {
            return all.stream().filter(e -> category.equalsIgnoreCase(e.getCategory()))
                    .collect(Collectors.toList());
        }

        return all;
    }

}
