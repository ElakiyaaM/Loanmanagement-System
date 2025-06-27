package main;

import dao.LoanRepositoryImpl;
import entity.Customer;
import entity.Loan;
import exception.InvalidLoanException;

import java.util.Scanner;

public class LoanManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LoanRepositoryImpl repo = new LoanRepositoryImpl();

        while (true) {
            System.out.println("\n--- Loan Management System ---");
            System.out.println("1. Apply Loan");
            System.out.println("2. Get All Loans");
            System.out.println("3. Get Loan by ID");
            System.out.println("4. Loan Repayment");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            try {
                switch (choice) {
                    case 1:
                        Customer cust = new Customer(1, "Elakiyaa", "elakiyaa@example.com", "1234567890", "Chennai", 700);
                        Loan loan = new Loan(101, cust, 500000, 8.5, 60, "HomeLoan", "Pending");
                        repo.applyLoan(loan);
                        break;

                    case 2:
                        for (Loan l : repo.getAllLoan()) {
                            l.printDetails();
                        }
                        break;

                    case 3:
                        System.out.print("Enter loan ID: ");
                        int id = sc.nextInt();
                        repo.getLoanById(id).printDetails();
                        break;

                    case 4:
                        System.out.print("Enter loan ID: ");
                        int lid = sc.nextInt();
                        System.out.print("Enter amount to repay: ");
                        double amt = sc.nextDouble();
                        repo.loanRepayment(lid, amt);
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (InvalidLoanException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

