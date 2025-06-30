package dao;

import entity.*;
import util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {

    public Customer findCustomerById(int customerId) {
        Customer customer = null;
        try (Connection conn = DBConnUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Customer WHERE customerId = ?");
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customer = new Customer(rs.getInt("customerId"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public Product findProductById(int productId) {
        Product product = null;
        try (Connection conn = DBConnUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Product WHERE productId = ?");
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                product = new Product(rs.getInt("productId"), rs.getString("name"), rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");
            while (rs.next()) {
                products.add(new Product(rs.getInt("productId"), rs.getString("name"), rs.getDouble("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void saveOrder(Order order) {
        try (Connection conn = DBConnUtil.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement orderStmt = conn.prepareStatement("INSERT INTO Orders (customerId, totalAmount) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, order.getCustomer().getCustomerId());
            orderStmt.setDouble(2, order.getTotalAmount());
            orderStmt.executeUpdate();
            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);
                for (OrderItem item : order.getItems()) {
                    PreparedStatement itemStmt = conn.prepareStatement("INSERT INTO OrderItem (orderId, productId, quantity, price) VALUES (?, ?, ?, ?)");
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProduct().getProductId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getPrice());
                    itemStmt.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}