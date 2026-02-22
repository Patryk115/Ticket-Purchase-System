# Ticket Purchase Workflow System (Camunda 8)

> An enterprise-level backend system orchestrating a complex cinema ticket purchasing process using **Spring Boot**, **PostgreSQL**, and **Camunda Platform 8** (BPMN & DMN).

## Project Overview
Unlike standard CRUD applications, this system is built on a **process-driven architecture**. The entire lifecycle of buying a ticket is orchestrated by a Camunda BPMN engine. The project features an interactive **CLI (Command Line Interface)** that guides the user step-by-step through the reservation process, handling dynamic inputs and real-time validations.

### System Roles
The architecture divides responsibilities into three logical actors:
1. **Client (CLI):** The end-user navigating the terminal, inputting data, and receiving the electronic ticket.
2. **Ticketing System:** The core orchestrator. Manages cinema repertoires, verifies seat availability, calculates dynamic discounts, and generates the final ticket.
3. **Payment Gateway Simulator:** Authorizes transactions, validates payment data (Card/BLIK/Transfer), and returns the status to the orchestrator.

## Core Business Logic & Process Flow

The system is highly resilient and handles multiple real-world scenarios and edge cases through Camunda Job Workers:

* **Interactive Seat Selection & Concurrency:** The system fetches cinemas, shows, and current reservations from PostgreSQL. When a user selects a seat, the `Check Seat Availability` worker verifies it. If the seat is already taken, an XOR gateway dynamically loops the user back to the selection stage.
* **Business Rules Engine (DMN):** After collecting user details and validating the email format, the system determines pricing. A Decision Model and Notation (DMN) table dynamically applies discounts based on the user's social category (*Student, Senior, Employee, etc.*).
* **Payment Processing & Validation:** Users can pay via Credit Card (validating length, expiration, CVV), BLIK (6-digit code validation), or Bank Transfer. If the `Payment Processing` worker declines the transaction, the workflow gracefully routes the user back to the payment entry step without losing their seat lock.
* **Ticket Generation & Delivery:** Upon successful payment, the system generates a ticket with a unique timestamp-based ID, saves it to the database, and simulates an email delivery (including QR code details) to the user's validated address.

## Tech Stack
* **Language & Framework:** Java 17+, Spring Boot
* **Orchestration:** Camunda Platform 8 (SaaS), Zeebe Engine
* **Modeling:** BPMN 2.0 (Business Process Model and Notation), DMN (Decision Model and Notation)
* **Database & ORM:** PostgreSQL, Spring Data JPA / Hibernate
* **Interface:** Interactive Java Terminal (CLI)

## Business Process Flow
<img width="1597" height="326" alt="image" src="https://github.com/user-attachments/assets/ad2887ec-1b57-4813-8779-b32673a59769" />


## How to Run Locally

To run this workflow system on your machine, you need a running instance of Camunda 8 (either Self-Managed via Docker or Camunda SaaS).

**1. Clone the repository:**
```bash
git clone [https://github.com/Patryk115/Ticket-Purchase-System.git](https://github.com/Patryk115/Ticket-Purchase-System.git)
cd Ticket-Purchase-System
```

**2. Configure Camunda Cluster:**
* Create a cluster in [Camunda 8 SaaS](https://camunda.io/).
* Generate Client Credentials.
* Open `src/main/resources/application.properties` and inject your cluster credentials:
```properties
zeebe.client.cloud.region=xxx
zeebe.client.cloud.clusterId=xxx
zeebe.client.cloud.clientId=xxx
zeebe.client.cloud.clientSecret=xxx
```

**3. Database Configuration:**
Ensure you have a PostgreSQL instance running and update the database URL, username, and password in your `application.properties`.

**4. Build and Run the App:**
```bash
mvn clean package
mvn exec:java
```
*Once running, the Spring Boot workers will connect to the Zeebe broker, and you can interact with the ticket purchasing system directly in your terminal.*
