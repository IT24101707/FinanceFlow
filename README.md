# 💰 FinanceFlow — Personal Finance Management System

FinanceFlow is a full-stack web application for tracking personal income and expenses, managing monthly budgets, and generating financial reports. Built with **Spring Boot**, **Thymeleaf**, and **MySQL**.

## ✨ Features

### 👤 User Management
- Secure registration & login (passwords hashed with BCrypt)
- Session-based authentication — each user only sees their own data
- Profile management (update name & phone)
- Change password

### 💰 Income Management
- Add, edit, delete income entries
- Search by source
- Filter by date or amount range

### 💸 Expense Management
- Add, edit, delete expense entries
- Categorized expenses: Food, Transport, Rent, Bills, Education, Entertainment, Shopping, Healthcare, Other
- Search / filter by category, date, or amount range

### 📊 Dashboard
- Total income, total expenses, current balance
- Monthly expense summary
- Top spending category
- Budget usage progress bar
- Income vs Expense chart (Chart.js)

### 📈 Reports
- Full report (all records)
- Monthly report
- Yearly report
- Category-wise report
- Export as **PDF** or **Excel**

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 22, Spring Boot 3.5 |
| Web / Templating | Spring MVC, Thymeleaf |
| Data Access | Spring Data JPA (Hibernate) |
| Database | MySQL |
| Security | Spring Security Crypto (BCrypt) |
| Frontend | Bootstrap 5, Chart.js |
| Reporting | iText (PDF), Apache POI (Excel) |
| Build Tool | Maven |

## 🚀 Getting Started

### Prerequisites
- Java 22+
- Maven
- MySQL Server running locally

### Setup

1. Clone the repository
   ```bash
   git clone <your-repo-url>
   cd financeflow
   ```

2. Create the database
   ```sql
   CREATE DATABASE financeflow;
   ```

3. Update database credentials in `src/main/resources/application.properties`
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/financeflow
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```

4. Run the application
   ```bash
   ./mvnw spring-boot:run
   ```

5. Open your browser at `http://localhost:8080/api/users/` to register an account.

## 📁 Project Structure

```
src/main/java/com/financeflow/financeflow/
├── config/       # Session/login interceptor & web config
├── controller/   # MVC controllers
├── entity/       # JPA entities
├── repository/   # Spring Data JPA repositories
└── service/      # Business logic (auth, reports, etc.)

src/main/resources/
├── templates/    # Thymeleaf HTML views
└── application.properties
```

## 🔒 Security Notes

- Passwords are hashed with BCrypt before being stored.
- All financial data is scoped to the logged-in user.
- Protected routes are guarded by a login interceptor.

## 📄 License

This project was built for academic purposes as part of a university coursework project.
