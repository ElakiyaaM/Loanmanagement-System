package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/loanmanagementsystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Elaki@11";  

    public static Connection getDBConn() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed:");
            e.printStackTrace();
            return null;
        }
    }
}
