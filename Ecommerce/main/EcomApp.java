package main;

import dao.*;
import entity.*;

import java.util.*;

public class EcomApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderProcessorRepository repo = new OrderProcessorRepositoryImpl();

        List<Product> products = repo.getAllProducts();
        for (Product p : products) {
            System.out.println(p.getProductId() + " - " + p.getName() + " - ₹" + p.getPrice());
        }

        System.out.print("Enter your customer ID: ");
        int customerId = sc.nextInt();
        Customer customer = repo.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        List<OrderItem> items = new ArrayList<>();
        String choice;
        do {
            System.out.print("Enter product ID: ");
            int productId = sc.nextInt();
            System.out.print("Enter quantity: ");
            int qty = sc.nextInt();
            Product product = repo.findProductById(productId);
            if (product != null) {
                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setQuantity(qty);
                item.setPrice(product.getPrice() * qty);
                items.add(item);
            } else {
                System.out.println("Product not found.");
            }
            System.out.print("Add more items? (yes/no): ");
            choice = sc.next();
        } while (choice.equalsIgnoreCase("yes"));

        double total = items.stream().mapToDouble(OrderItem::getPrice).sum();

        Order order = new Order();
        order.setCustomer(customer);
        order.setItems(items);
        order.setTotalAmount(total);

        repo.saveOrder(order);
        System.out.println("Order placed successfully!");
    }
}