package dao;

import entity.Customer;
import entity.Loan;
import exception.InvalidLoanException;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRepositoryImpl implements ILoanRepository {

    @Override
    public void applyLoan(Loan loan) throws Exception {
        String sql = "INSERT INTO Loan (loanId, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getDBConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, loan.getLoanId());
            pstmt.setInt(2, loan.getCustomer().getCustomerId());
            pstmt.setDouble(3, loan.getPrincipalAmount());
            pstmt.setDouble(4, loan.getInterestRate());
            pstmt.setInt(5, loan.getLoanTerm());
            pstmt.setString(6, loan.getLoanType());
            pstmt.setString(7, loan.getLoanStatus());

            pstmt.executeUpdate();
            System.out.println("Loan applied successfully.");
        }
    }

    @Override
    public double calculateInterest(int loanId) throws InvalidLoanException {
        Loan loan = getLoanById(loanId);
        return calculateInterest(loan.getPrincipalAmount(), loan.getInterestRate(), loan.getLoanTerm());
    }

    @Override
    public double calculateInterest(double principal, double rate, int term) {
        return (principal * rate * term) / 1200;
    }

    @Override
    public void loanStatus(int loanId) throws InvalidLoanException {
        Loan loan = getLoanById(loanId);
        Customer customer = loan.getCustomer();
        String newStatus = customer.getCreditScore() > 650 ? "Approved" : "Rejected";

        try (Connection conn = DBUtil.getDBConn();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Loan SET loanStatus=? WHERE loanId=?")) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, loanId);
            pstmt.executeUpdate();

            System.out.println("Loan status updated to: " + newStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double calculateEMI(int loanId) throws InvalidLoanException {
        Loan loan = getLoanById(loanId);
        return calculateEMI(loan.getPrincipalAmount(), loan.getInterestRate(), loan.getLoanTerm());
    }

    @Override
    public double calculateEMI(double principal, double annualRate, int term) {
        double monthlyRate = annualRate / 12 / 100;
        return (principal * monthlyRate * Math.pow(1 + monthlyRate, term)) /
               (Math.pow(1 + monthlyRate, term) - 1);
    }

    @Override
    public int loanRepayment(int loanId, double amount) throws InvalidLoanException {
        double emi = calculateEMI(loanId);
        int numEmis = (int) (amount / emi);
        System.out.println("Amount can cover " + numEmis + " EMIs.");
        return numEmis;
    }

    @Override
    public List<Loan> getAllLoan() throws Exception {
        List<Loan> loanList = new ArrayList<>();
        String sql = "SELECT l.*, c.* FROM Loan l JOIN Customer c ON l.customerId = c.customerId";

        try (Connection conn = DBUtil.getDBConn();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Customer cust = new Customer(
                        rs.getInt("customerId"),
                        rs.getString("name"),
                        rs.getString("emailAddress"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getInt("creditScore")
                );

                Loan loan = new Loan(
                        rs.getInt("loanId"),
                        cust,
                        rs.getDouble("principalAmount"),
                        rs.getDouble("interestRate"),
                        rs.getInt("loanTerm"),
                        rs.getString("loanType"),
                        rs.getString("loanStatus")
                );

                loanList.add(loan);
            }
        }
        return loanList;
    }

    @Override
    public Loan getLoanById(int loanId) throws InvalidLoanException {
        String sql = "SELECT l.*, c.* FROM Loan l JOIN Customer c ON l.customerId = c.customerId WHERE l.loanId = ?";
        try (Connection conn = DBUtil.getDBConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, loanId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Customer cust = new Customer(
                            rs.getInt("customerId"),
                            rs.getString("name"),
                            rs.getString("emailAddress"),
                            rs.getString("phoneNumber"),
                            rs.getString("address"),
                            rs.getInt("creditScore")
                    );

                    return new Loan(
                            rs.getInt("loanId"),
                            cust,
                            rs.getDouble("principalAmount"),
                            rs.getDouble("interestRate"),
                            rs.getInt("loanTerm"),
                            rs.getString("loanType"),
                            rs.getString("loanStatus")
                    );
                } else {
                    throw new InvalidLoanException("Loan ID not found: " + loanId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidLoanException("DB error while retrieving loan");
        }
    }
}
