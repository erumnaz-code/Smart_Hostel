# 🏨 Smart Hostel Management System

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue?style=flat-square&logo=mysql)
![JDBC](https://img.shields.io/badge/JDBC-Connected-green?style=flat-square)
![OOP](https://img.shields.io/badge/OOP-Concepts-purple?style=flat-square)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=flat-square)

---

## 📌 Project Description

The **Smart Hostel Management System** is a Java-based desktop application developed as a semester project to demonstrate Object-Oriented Programming (OOP) principles in a real-world scenario. The system provides a comprehensive, role-based platform for managing hostel operations including student registration, meal attendance tracking, billing, mess menu management, and complaint handling. It is designed for three types of users — **Admin**, **Mess In-Charge**, and **Students** — each with a dedicated dashboard and a tailored set of features. The application connects to a **MySQL** database via **JDBC** and presents a user-friendly graphical interface built with **Java Swing**.

---

## Group Members

| # | Full Name        | CMS / Student ID | Section  |
| - | ---------------- | ---------------- | ---------|
| 1 | Erum Naz         | 053-25-0010      | A        |
| 2 | Falaq Aftab Qazi | 053-25-0011      | A        |
| 3 | Muhammad Tarique | 053-25-0031      | A        |

---



---

## Features

### Authentication & Access Control
- Secure login system with role-based access (Admin / Mess In-Charge / Student)
- New student self-registration with automatic account linking
- Role detection redirects each user to their respective dashboard upon login

### Admin Dashboard
- Add, update, and delete student records
- View all registered students in a table
- Generate and manage monthly billing for all students
- View and update complaint statuses (Pending / Resolved)
- Full oversight of the entire hostel system

### Mess In-Charge Dashboard
- Manage the weekly mess menu (Breakfast, Lunch, Dinner)
- Mark daily meal attendance per student and meal type (Present / Absent)
- Set and update per-meal pricing
- Generate monthly bills automatically based on attendance records
- View all student bills and mark bills as Paid

### Student Dashboard
- View personal profile and room details
- Check meal attendance history
- View monthly billing statements and payment status
- Submit complaints and track their resolution status
- Browse the current mess menu

---

## Technologies Used

| Technology | Purpose |
|------------|---------|
| **Java (JDK 17+)** | Core programming language |
| **Java Swing** | Graphical User Interface (GUI) |
| **OOP Concepts** | System design and architecture |
| **MySQL 8.0+** | Relational database for persistent storage |
| **JDBC** | Java–MySQL database connectivity |
| **Collections Framework** | `ArrayList`, `List` for data management |
| **Exception Handling** | Robust error management throughout the app |
| **MySQL Connector/J 9.6.0** | JDBC driver for MySQL (`mysql-connector-j-9.6.0.jar`) |

---

## OOP Concepts Implemented

### Classes & Objects
Every entity in the system — `User`, `Student`, `Admin`, `MessInCharge`, `Billing`, `Attendance`, `Complaint`, and `Menu` — is modeled as a class with its own attributes and behaviors, and instantiated as objects throughout the application.

### Inheritance
The `User` class serves as the base (parent) class. `Student`, `Admin`, and `MessInCharge` all extend `User`, inheriting common properties such as `id`, `username`, `password`, and `role`, while adding their own specialized attributes.

### Polymorphism
The `getDescription()` method is defined in the `User` base class and overridden in `Student`, `Admin`, and `MessInCharge` subclasses. At runtime, the appropriate version is called based on the actual object type, demonstrating runtime polymorphism via method overriding.

### Encapsulation
All model class fields are declared `private`, and accessed or modified exclusively through `public` getter and setter methods. This protects data integrity and hides internal implementation details from external code.

### Abstraction
The application is structured in layers — GUI, Service, DAO, and Model — where each layer exposes only what is necessary to the layer above it. Users interact with the GUI without any awareness of the underlying database operations.

### Interfaces
The DAO (Data Access Object) layer acts as an abstraction boundary between the business logic (Service layer) and the database. Each DAO class encapsulates all SQL operations for its respective entity, providing a consistent interface to the rest of the application.

### Exception Handling
`try-catch` blocks are used throughout the DAO and Service layers to gracefully handle `SQLException` and other runtime errors, preventing application crashes and providing meaningful error output for debugging.

---

## Project Modules

### `model` — Data Models
Contains all entity classes representing the core data structures of the system.

- `User.java` — Base class with common user attributes (`id`, `username`, `password`, `role`)
- `Student.java` — Extends `User`; includes CMS ID, name, room number, department, and contact
- `Admin.java` — Extends `User`; represents admin with full system access
- `MessInCharge.java` — Extends `User`; represents the mess staff role
- `Billing.java` — Holds billing data (student ID, month, total amount, status)
- `Attendance.java` — Records meal attendance (date, meal type, status, price)
- `Complaint.java` — Stores student complaints with status tracking
- `Menu.java` — Represents daily meal menu entries

### `db` — Database Connectivity
- `DatabaseConnection.java` — Singleton class that establishes and returns the JDBC connection to MySQL
- `TestDB.java` — Utility class used to test database connectivity during development

### `dao` — Data Access Objects
Each DAO class handles all CRUD operations for its corresponding database table using JDBC and `PreparedStatement`.

- `StudentDAO.java` — Add, update, delete, and retrieve student records
- `UserDAO.java` — Register users and link them to student records
- `AttendanceDAO.java` — Mark and retrieve meal attendance
- `BillingDAO.java` — Save, update, and retrieve billing records; mark bills as paid
- `ComplaintDAO.java` — Submit and update student complaints
- `MenuDAO.java` — Add, update, and retrieve mess menu items

### `service` — Business Logic
Service classes sit between the GUI and the DAO layer, handling validation and orchestrating DAO calls.

- `LoginService.java` — Authenticates users and returns their role
- `RegisterService.java` — Handles new student and user account creation
- `StudentService.java` — Delegates student management operations
- `AttendanceService.java` — Processes attendance logic
- `BillingService.java` — Calculates and manages monthly bills
- `ComplaintService.java` — Manages complaint submission and status updates
- `MenuService.java` — Handles menu creation and retrieval

### `gui` — Graphical User Interface
All Swing-based screens for user interaction.

- `LoginScreen.java` — Entry point; authenticates users and routes to the correct dashboard
- `RegisterScreen.java` — Student self-registration form
- `AdminDashboard.java` — Full management panel for the Admin
- `MessDashboard.java` — Attendance, menu, and billing management for Mess In-Charge
- `StudentDashboard.java` — Student-facing view for profile, bills, attendance, and complaints

### `Main.java`
The application entry point. Launches the `LoginScreen` on the Event Dispatch Thread (EDT) using `SwingUtilities.invokeLater()`.

---

## How to Run

### Prerequisites
- **Java JDK 17** or higher
- **MySQL Server 8.0** or higher
- MySQL Connector/J driver (already included at `src/mysql-connector-j-9.6.0.jar`)

### 1. Database Setup

```sql
-- 1. Create the database
CREATE DATABASE smart_hostel;
USE smart_hostel;

-- 2. Run the provided SQL setup script
-- Import the schema from: database_setup.docx (refer to the file for full SQL)
```

> Update the database credentials in `src/db/DatabaseConnection.java` if needed:
> ```java
> private static final String db_URL = "jdbc:mysql://localhost:3306/smart_hostel";
> private static final String db_User = "your_username";
> private static final String db_Password = "your_password";
> ```

### 2. Compile the Project

Navigate to the `src/` folder and compile with the JDBC driver on the classpath:

```bash
cd SmartHostel/src

# On Windows
javac -cp ".;mysql-connector-j-9.6.0.jar" model/*.java db/*.java dao/*.java service/*.java gui/*.java Main.java

# On macOS / Linux
javac -cp ".:mysql-connector-j-9.6.0.jar" model/*.java db/*.java dao/*.java service/*.java gui/*.java Main.java
```

### 3. Run the Application

```bash
# On Windows
java -cp ".;mysql-connector-j-9.6.0.jar" Main

# On macOS / Linux
java -cp ".:mysql-connector-j-9.6.0.jar" Main
```

---

## Project Structure

```
SmartHostel/
├── database_setup.docx              # SQL schema and setup instructions
├── Smart Hostel Management System_Final_Project_Report.pdf
└── src/
    ├── Main.java                    # Application entry point
    ├── mysql-connector-j-9.6.0.jar  # JDBC driver
    │
    ├── model/                       # Entity / data model classes
    │   ├── User.java
    │   ├── Student.java
    │   ├── Admin.java
    │   ├── MessInCharge.java
    │   ├── Attendance.java
    │   ├── Billing.java
    │   ├── Complaint.java
    │   └── Menu.java
    │
    ├── db/                          # Database connectivity
    │   ├── DatabaseConnection.java
    │   └── TestDB.java
    │
    ├── dao/                         # Data Access Objects (JDBC operations)
    │   ├── UserDAO.java
    │   ├── StudentDAO.java
    │   ├── AttendanceDAO.java
    │   ├── BillingDAO.java
    │   ├── ComplaintDAO.java
    │   └── MenuDAO.java
    │
    ├── service/                     # Business logic layer
    │   ├── LoginService.java
    │   ├── RegisterService.java
    │   ├── StudentService.java
    │   ├── AttendanceService.java
    │   ├── BillingService.java
    │   ├── ComplaintService.java
    │   └── MenuService.java
    │
    └── gui/                         # Swing GUI screens
        ├── LoginScreen.java
        ├── RegisterScreen.java
        ├── AdminDashboard.java
        ├── MessDashboard.java
        └── StudentDashboard.java
```

---

## Video Demo

> **YouTube Demo:** [Watch Here](https://www.youtube.com/watch?v=YOUR_VIDEO_ID)

---

## GitHub Repository

> **GitHub:** [https://github.com/YOUR_USERNAME/SmartHostel](https://github.com/YOUR_USERNAME/SmartHostel)

---

## Academic Integrity Statement

This project was developed entirely by the group members listed above as a requirement for the **Object-Oriented Programming** course. All code, design decisions, and documentation are original work produced for academic purposes. Any external resources or references used have been appropriately acknowledged. This project is not intended for commercial use.

---

<div align="center">
  <sub>Developed with ❤️ for OOP Semester Project — [Your University Name]</sub>
</div>
