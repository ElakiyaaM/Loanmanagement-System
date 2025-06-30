package dao;

import entity.*;

import java.util.List;

public interface OrderProcessorRepository {
    Customer findCustomerById(int customerId);
    Product findProductById(int productId);
    List<Product> getAllProducts();
    void saveOrder(Order order);
}