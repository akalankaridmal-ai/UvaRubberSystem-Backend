# Uva Rubber Collection Management System (Backend)

An automated Enterprise Resource Planning (ERP) backend solution designed for rubber collection centers to digitize manual ledger processes, automate complex industrial calculations, and manage supplier payment cycles.

## 📌 Project Overview
In many rubber collection centers, daily transactions—including weighing, Metrolac-based Dry Rubber Content (DRC) calculation, and payment scheduling—are handled manually. This project provides a robust Java-based backend to automate these workflows, ensuring data accuracy and financial transparency.

## 🚀 Key Features
- **Automated DRC Calculation:** Implements industrial formulas to convert Metrolac readings to DRC percentages and calculate total dry weight (Dry KG).
- **Staggered Payment Management:** Automatically assigns collection records to specific payment windows (1st-15th paid on the 25th; 16th-31st paid on the 10th of the following month).
- **Supplier Database:** Manages farmer profiles including bank details (BOC, PPLS, RDB) and account information for automated bank sheet generation.
- **Data Persistence:** Integrated with a MySQL relational database using the DAO (Data Access Object) pattern.

## 🛠️ Tech Stack
- **Language:** Java SE (Core Java)
- **Database:** MySQL
- **Connectivity:** JDBC (MySQL Connector/J)
- **Architecture:** Layered Architecture (Model-View-Controller principles)
- **Design Patterns:** DAO Pattern, Singleton Pattern (for Database Connection)

## 📁 Project Structure
```text
src/com/uvarubber/
├── dao/          # Data Access Objects (SQL Logic)
├── model/        # Data Entities (Supplier, Collection)
├── service/      # Business Logic (Calculations & Rules)
├── util/         # Database Connection & Utilities
└── Main.java     # System Entry Point & Testing
