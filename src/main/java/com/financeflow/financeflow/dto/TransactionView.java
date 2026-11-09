package com.financeflow.financeflow.dto;

// Lightweight view object used to show Income + Expense together
// as a single "Recent Transactions" list on the dashboard.
public class TransactionView {

    private String date;
    private String description;
    private String category;
    private String type; // "Income" or "Expense"
    private Double amount;

    public TransactionView(String date, String description, String category, String type, Double amount) {
        this.date = date;
        this.description = description;
        this.category = category;
        this.type = type;
        this.amount = amount;
    }

    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getType() { return type; }
    public Double getAmount() { return amount; }

}
