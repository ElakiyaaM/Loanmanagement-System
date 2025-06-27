package dao;

import entity.Loan;
import exception.InvalidLoanException;
import java.util.List;

public interface ILoanRepository {
    void applyLoan(Loan loan) throws Exception;

    double calculateInterest(int loanId) throws InvalidLoanException;
    double calculateInterest(double principal, double rate, int term);

    void loanStatus(int loanId) throws InvalidLoanException;

    double calculateEMI(int loanId) throws InvalidLoanException;
    double calculateEMI(double principal, double rate, int term);

    int loanRepayment(int loanId, double amount) throws InvalidLoanException;

    List<Loan> getAllLoan() throws Exception;

    Loan getLoanById(int loanId) throws InvalidLoanException;
}

