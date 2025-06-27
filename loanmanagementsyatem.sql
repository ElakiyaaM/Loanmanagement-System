
CREATE DATABASE LoanManagementSystem;
USE LoanManagementSystem;


CREATE TABLE Customer (
    customerId INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    emailAddress VARCHAR(100) UNIQUE NOT NULL,
    phoneNumber VARCHAR(15),
    address VARCHAR(255),
    creditScore INT CHECK (creditScore BETWEEN 300 AND 900)
);


CREATE TABLE Loan (
    loanId INT PRIMARY KEY,
    customerId INT,
    principalAmount DECIMAL(15,2),
    interestRate DECIMAL(5,2),
    loanTerm INT,  -- in months
    loanType VARCHAR(20) CHECK (loanType IN ('CarLoan', 'HomeLoan')),
    loanStatus VARCHAR(20) CHECK (loanStatus IN ('Pending', 'Approved')),
    
    FOREIGN KEY (customerId) REFERENCES Customer(customerId)
);


INSERT INTO Customer (customerId, name, emailAddress, phoneNumber, address, creditScore)
VALUES
(1, 'Elakiyaa M', 'elakiyaa@example.com', '9876543210', 'Chennai, India', 750),
(2, 'Arjun R', 'arjunr@example.com', '9988776655', 'Bangalore, India', 820);


INSERT INTO Loan (loanId, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus)
VALUES
(101, 1, 500000.00, 8.5, 60, 'HomeLoan', 'Pending'),
(102, 2, 300000.00, 9.0, 36, 'CarLoan', 'Approved');


SELECT * FROM Customer;


SELECT l.loanId, c.name AS customerName, l.principalAmount, l.loanType, l.loanStatus
FROM Loan l
JOIN Customer c ON l.customerId = c.customerId;


SELECT * FROM Loan WHERE loanStatus = 'Pending';


SELECT * FROM Loan WHERE loanType = 'HomeLoan';


SELECT c.name, c.creditScore, l.loanId, l.loanStatus
FROM Customer c
JOIN Loan l ON c.customerId = l.customerId
WHERE l.loanStatus = 'Approved';
