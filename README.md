# BankOps: Spring Boot API for Bank Account Management
This project implements a Spring Boot API for managing bank accounts, allowing users to perform various operations like deposits, withdrawals, transfers, and account balance inquiries.

# Prerequisites
Git installed on your system
Docker installed and running
A code editor or IDE of your choice (e.g.,Eclipse, IntelliJ IDEA)

# Setting Up the Project

# 1) Clone the Repository:

 
Bash
git clone https://github.com/kaan-kucuk/BankOps.git

# 2) Navigate to the Project Directory:

Bash
cd BankOps

# 3) Run the Application:

Start the containerized application using Docker Compose:

Bash
docker-compose up

 # Using the API
Once the application is running, you can use a tool like Postman or curl to interact with the API endpoints. Here are some examples:

# Get All Accounts:

GET http://localhost:9090/api/accounts/getAll
# Get Account Balance:

GET http://localhost:9090/api/accounts/getBalance/{accountNumber}
Replace {accountNumber} with the actual account number you want to retrieve the balance for.

# Deposit Money:

POST http://localhost:9090/api/operations/deposit
Content-Type: application/json

{
  "amountOfDeposit": 100.00,
  
  "accountNumber": "1234567890"
}
# Withdraw Money:

POST http://localhost:9090/api/operations/withdraw
Content-Type: application/json

{
  "amountOfWithdraw": 50.00,
  
  "accountNumber": "1234567890"
}
# Transfer Money:

PUT http://localhost:9090/api/operations/transfer
Content-Type: application/json

{
  "fromAccount": "1234567890",
  
  "toAccount": "9876543210",
  
  "amount": 25.00
}
# Get Transaction History:

GET http://localhost:9090/api/operations/transactions/{accountNumber}
Replace {accountNumber} with the account number for which you want to retrieve the transaction history.
