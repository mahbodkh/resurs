#### **Project Overview**

This project is a **Spring Boot** application designed to handle **customer transactions** and compute a **customer score** based on their transaction history and account balance. The project involves working with **REST APIs**, **database connections**, and integrating with a **third-party service** to retrieve account balances.

You are tasked with building a set of features, including reading transaction data from a database, calculating a customer score using a given formula, and integrating with external and internal APIs. The goal is to assess your ability to work with Spring Boot, handle REST APIs, work with databases, and design a solution that is efficient, scalable, and maintainable.

---

#### **Project Setup**

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   ```

2. **Navigate to the project directory:**
   ```bash
   cd interview
   ```

3. **Build the project using Gradle:**
   ```bash
   ./gradlew build
   ```

4. **Run the project:**
   ```bash
   ./gradlew bootRun
   ```

5. **Access the API documentation** (Swagger, if set up):
   ```
   http://localhost:8080/swagger-ui.html
   ```

---

#### **Project Requirements**

1. **Database Connection**:
    - You should set up a connection to a relational database (e.g., MySQL, PostgreSQL, or H2).
    - Create a `Transaction` table with fields like `transactionId`, `customerId`, `amount`, `transactionDate`, and `transactionType`.

2. **REST API Endpoints**:
   You need to expose the following endpoints:

   - **`POST /api/v1/customer`**
      - Create new Customer.

   - **`GET /api/v1/customer/{customerId}`**
     - Retrieve  a specific customer.
     - The `customerId` will be provided, and you should return that customer information.

   - **`POST /api/v1/customers/{customerId}/creditScore`**
     - Calculate customer score.
     - Use a given formula to calculate the score.
     
   - **`POST /api/v1/customers/{customerId}/loan`**
      - request a loan.
      - check if the customer can get loan and bank has capability to get loan and then save the custome.

3. **Customer Score Calculation**:
   The customer score should be computed based on the following:
    - **Current account balance**: You should simulate or retrieve this value using a mock third-party service.
    - **Transaction history**: Use the transactions stored in the database (total transactions, average transaction amount).

   Formula Example:
   ```java
   score = (normalizedBalance * 0.5) + (averageTransactionAmount * 0.3) + (transactionCount * 0.2)
   ```

4. **Integration with External Service**:
   You are provided with a **mock third-party API** (via `MockedTaxCheckGateway`) to retrieve the **customer's current account balance**. The API will return a sample response containing the `accountId`, `balance`, `currency`, and `lastUpdated` fields.

   Use the account balance as an input for the customer score calculation.

---

#### **Tasks & Expectations**

- **Entity Design**:
    - Create a **`Customer`** and **`Transaction`** entity class that maps to the database and represents a customer transaction.

- **Repository**:
    - Implement a **JPA Repository** to retrieve transactions based on the `customerId`.

- **Service Layer**:
    - Write a service that handles the business logic of fetching the customer's transactions, calculating the average transaction amount, and computing the customer score.

- **Third-Party Integration**:
    - Simulate or mock the third-party API that provides the customer’s current balance.
    - Add **random delays** and **rate limits** to simulate real-world conditions for external service calls (e.g., one call per time, no concurrency).
---

#### **Optional Bonus Tasks**

- Implement **API versioning** (e.g., `/api/v1` vs `/api/v2`).
- Add **error handling** for external API calls and database interactions.
- Improve the score formula based on additional factors, such as **credit risk** or **loan history**.
- Write **unit tests** for the service layer and API endpoints using **JUnit** and **Mockito**.

---

#### **Submission Guidelines**

- Fork this repository or download the codebase.
- Implement the required features and ensure that the application builds and runs successfully.
- Provide test cases where appropriate.
- Push your changes to your own repository and submit the link.

---

#### **Example Input & Output**

**Example Transaction Data (in the database)**:

| transactionId | customerId | amount | transactionDate      | transactionType |
|---------------|------------|--------|----------------------|-----------------|
| 1             | 1001       | 500.00 | 2023-01-15 10:30:00  | DEBIT           |
| 2             | 1001       | 1200.50| 2023-02-20 14:15:00  | CREDIT          |
| 3             | 1001       | 300.75 | 2023-03-05 08:50:00  | DEBIT           |

**Example API Request/Response**:

1. **Get Transactions by Customer**:
    - `GET /api/v1/transactions/customer/1001`
   ```json
   [
       {
           "transactionId": 1,
           "customerId": 1001,
           "amount": 500.00,
           "transactionDate": "2023-01-15T10:30:00",
           "transactionType": "DEBIT"
       },
       {
           "transactionId": 2,
           "customerId": 1001,
           "amount": 1200.50,
           "transactionDate": "2023-02-20T14:15:00",
           "transactionType": "CREDIT"
       }
   ]
   ```

2. **Get Average Transaction Amount**:
    - `GET /api/v1/transactions/customer/1001/average`
   ```json
   675.25
   ```

3. **Get Customer Score**:
    - `GET /api/v1/customers/1001/score`
   ```json
   {
       "customerId": 1001,
       "score": 78.5
   }
   ```

---

#### **Technologies & Tools**

- **Java 17+**
- **Spring Boot 3.1+**
- **Gradle** as the build tool
- **H2 / MySQL / PostgreSQL / MongoDB** (choose based on preference)
- **JPA / Hibernate** for database access
- **RestTemplate / WebClient** for third-party API integration

---

Good luck, and we look forward to reviewing your solution!