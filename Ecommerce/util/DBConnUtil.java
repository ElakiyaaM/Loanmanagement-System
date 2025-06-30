package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBPropertyUtil.getConnectionString(), "root", "Elaki@11");
    }
}