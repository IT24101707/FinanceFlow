# 💰 FinanceFlow – Personal Finance Management System

**FinanceFlow** is a full-stack web application developed as an individual project to help users manage their personal finances efficiently. It allows users to track income and expenses, manage monthly budgets, monitor financial activities, and generate detailed financial reports through a user-friendly dashboard.

## ✨ Key Features

### 👤 User Management
- Secure user registration and login
- Password hashing using BCrypt
- Session-based authentication
- User profile management
- Change password functionality
- User-specific financial data access

### 💰 Income Management
- Add, edit, and delete income records
- Search income by source
- Filter records by date and amount range

### 💸 Expense Management
- Add, edit, and delete expense records
- Categorize expenses into:
  - Food
  - Transport
  - Rent
  - Bills
  - Education
  - Entertainment
  - Shopping
  - Healthcare
  - Other
- Search and filter expenses by category, date, and amount range

### 📊 Interactive Dashboard
- Display total income and total expenses
- View current balance
- Monthly expense summary
- Identify top spending category
- Track budget usage with a progress bar
- Visualize income vs. expenses using Chart.js

### 📈 Financial Reports
- Full financial report
- Monthly report
- Yearly report
- Category-wise report
- Export reports as PDF
- Export reports as Excel files

## 🛠️ Technologies Used

| Layer | Technology |
|---|---|
| Programming Language | Java 22 |
| Backend Framework | Spring Boot 3.5 |
| Web Framework | Spring MVC |
| Template Engine | Thymeleaf |
| Data Access | Spring Data JPA / Hibernate |
| Database | MySQL |
| Security | Spring Security Crypto (BCrypt) |
| Frontend | HTML, CSS, Bootstrap 5 |
| Data Visualization | Chart.js |
| PDF Generation | iText |
| Excel Generation | Apache POI |
| Build Tool | Maven |

## 🚀 Getting Started

### Prerequisites

Make sure you have installed:

- Java 22 or later
- MySQL Server
- Maven (or use the included Maven Wrapper)
- IntelliJ IDEA or another Java IDE

### Installation and Setup

**1. Clone the repository**

```bash
git clone https://github.com/IT24101707/FinanceFlow.git
cd FinanceFlow
```

**2. Create the MySQL database**

Open MySQL Workbench and execute:

```sql
CREATE DATABASE financeflow;
```

**3. Configure database credentials**

Update your local `src/main/resources/application.properties` file with your MySQL connection details.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/financeflow
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Keep your actual database credentials private. Do not commit passwords or sensitive information to GitHub.

**4. Run the application**

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Alternatively, run the main Spring Boot application class from your IDE.

**5. Open the application**

Visit the following URL in your browser to access the registration page:

```text
http://localhost:8080/api/users/
```

## 📁 Project Structure

```text
FinanceFlow/
│
├── src/
│   ├── main/
│   │   ├── java/com/financeflow/financeflow/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   │
│   │   └── resources/
│   │       ├── templates/
│   │       └── application.properties
│   │
│   └── test/
│
├── .mvn/
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## 🔒 Security

- User passwords are hashed using BCrypt.
- Financial records are scoped to the logged-in user.
- Protected application routes are guarded by a login interceptor.

## 📚 Learning Outcomes

Developing FinanceFlow independently helped me gain practical experience in:

- Java and Spring Boot Development
- MVC Architecture
- CRUD Operations
- Spring Data JPA and Hibernate
- MySQL Database Management
- Session-based Authentication
- Financial Dashboard Development
- PDF and Excel Report Generation
- Full-Stack Web Application Development

## 👩‍💻 Developer

**Adini Uththara Amarakoon**

BSc (Hons) in Information Technology  
Specialization in Information Systems Engineering  
Sri Lanka Institute of Information Technology (SLIIT)

---

⭐ Thank you for exploring FinanceFlow! Feedback and suggestions are welcome.

